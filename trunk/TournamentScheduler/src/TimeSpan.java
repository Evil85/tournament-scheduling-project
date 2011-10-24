import java.text.DateFormat;
import java.sql.Timestamp;

public class TimeSpan implements Comparable<TimeSpan> {
	
    /**
     * A wrapper on two Timestamp objects which provides a bit of
     * extra functionality.
     * @param start
     * @param end
     */
	public TimeSpan(Timestamp start, Timestamp end) {
		m_start = start;
		m_end = end;
		Validate();
		CalculateMinutes();
	}
	
	/**
	 * Overlapping TimeSpans are return 0 in order to get the desired behavior in a SortedSet.
	 */
	public int compareTo(TimeSpan o) {
		if (Overlaps(o))
		{
			return 0;
		}
		else if (m_end.compareTo(o.m_start) <= 0)
		{
			return -1;
		}
		else
		{
			assert m_start.compareTo(o.m_end) >= 0;
			return 1;
		}
	}
	
	public String toString()
	{
		DateFormat shortDate = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		
		String strStartDate = shortDate.format(m_start);
		String strEndDate = shortDate.format(m_end);
		String strStartTime = shortTime.format(m_start);
		String strEndTime = shortTime.format(m_end);
		
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
        return (other.m_start.compareTo(m_start) <= 0 && other.m_end.compareTo(m_end) >= 0) ||
            (other.m_start.after(m_start) && other.m_start.before(m_end)) ||
            (other.m_end.before(m_end) && other.m_end.after(m_start));
    }
	
	/**
	 * A fairly harsh validation of the start and end times.
	 */
	private void Validate()
	{
		assert m_start.before(m_end);
		assert m_start.getTime() % c_nMsPerMinute == 0;
		assert m_start.getNanos() == 0;
		assert m_end.getTime() % c_nMsPerMinute == 0;
		assert m_end.getNanos() == 0;
	}
	
	/**
	 * Calculates the duration of this TimeSpan.
	 */
	private void CalculateMinutes()
	{
		m_nMinutes = ((int)m_end.getTime() - (int)m_start.getTime()) / c_nMsPerMinute;
	}
	
	public Timestamp m_start;
	public Timestamp m_end;
	public int m_nMinutes;
	
	private static final int c_nMsPerMinute = 60000;
}
