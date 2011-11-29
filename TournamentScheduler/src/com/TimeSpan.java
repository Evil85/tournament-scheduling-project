package com;

import java.text.DateFormat;
import java.util.Collection;
import java.sql.Timestamp;

public class TimeSpan implements Comparable<TimeSpan> {

    /**
     * A wrapper on two Timestamp objects which provides a bit of
     * extra functionality.
     * @param start
     * @param end
     */
	public TimeSpan(Timestamp start, Timestamp end)
	{
		setStart(start);
		setEnd(end);
		Validate();
		CalculateMinutes();
	}

	public TimeSpan(int minutes, Timestamp end)
	{
		setStart(new Timestamp(end.getTime() - minutes * c_nMsPerMinute));
		setEnd(end);
		Validate();
		setMinutes(minutes);
	}

	/**
	 * Overlapping TimeSpans are return 0 in order to get the desired behavior in a SortedSet.
	 */
	public int compareTo(TimeSpan o) {
		if (Overlaps(o))
		{
			return 0;
		}
		else if (getEnd().compareTo(o.getStart()) <= 0)
		{
			return -1;
		}
		else
		{
			assert getStart().compareTo(o.getEnd()) >= 0;
			return 1;
		}
	}

	public String toString()
	{
		DateFormat shortDate = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);

		String strStartDate = shortDate.format(getStart());
		String strEndDate = shortDate.format(getEnd());
		String strStartTime = shortTime.format(getStart());
		String strEndTime = shortTime.format(getEnd());

		if (strEndDate.equals(strStartDate))
			return String.format("%s %s - %s", strStartDate, strStartTime, strEndTime);
		else
			return String.format("%s %s - %s %s", strStartDate, strStartTime, strEndDate, strEndTime);
	}

	/**
	 * Check to see whether or not this TimeSpan overlaps with another.
	 * @param other
	 * @return
	 */
	public boolean Overlaps(TimeSpan other)
    {
        return (!other.getStart().after(getStart()) && !other.getEnd().before(getEnd())) ||
            (other.getStart().after(getStart()) && other.getStart().before(getEnd())) ||
            (other.getEnd().before(getEnd()) && other.getEnd().after(getStart()));
    }

	/**
	 * Determines whether this TimeSpan is contained within at least one of the specified TimeSpans.
	 * @param others
	 * @return
	 */
	public boolean Within(Collection<TimeSpan> others)
	{
		return Within(others.toArray(new TimeSpan[0]));
	}

	public boolean Within(TimeSpan... others)
	{
		for (TimeSpan o : others)
			if (!(o.getStart().after(getStart()) || o.getEnd().before(getEnd())))
				return true;

		return false;
	}

	public Timestamp getStart() {
		return m_start;
	}

	private void setStart(Timestamp m_start) {
		this.m_start = m_start;
	}

	public Timestamp getEnd() {
		return m_end;
	}

	private void setEnd(Timestamp m_end) {
		this.m_end = m_end;
	}

	public int getMinutes() {
		return m_nMinutes;
	}

	private void setMinutes(int m_nMinutes) {
		this.m_nMinutes = m_nMinutes;
	}

	/**
	 * A fairly harsh validation of the start and end times.
	 */
	private void Validate()
	{
		if (!(getStart().before(getEnd()) &&
		getStart().getTime() % c_nMsPerMinute == 0 &&
		getStart().getNanos() == 0 &&
		getEnd().getTime() % c_nMsPerMinute == 0 &&
		getEnd().getNanos() == 0))
			throw new IllegalArgumentException();
	}

	/**
	 * Calculates the duration of this TimeSpan.
	 */
	private void CalculateMinutes()
	{
		setMinutes(((int)getEnd().getTime() - (int)getStart().getTime()) / c_nMsPerMinute);
	}

	private Timestamp m_start;
	private Timestamp m_end;
	private int m_nMinutes;

	private static final int c_nMsPerMinute = 60000;
}
