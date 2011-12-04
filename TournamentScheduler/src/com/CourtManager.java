package com;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;



public class CourtManager {

	public CourtManager(Court... courts)
	{
		m_courts = courts;
	}

	public CourtManager(List<Court> courts)
	{
		m_courts = new Court[courts.size()];
		for (int i = 0; i < courts.size(); i++)
			m_courts[i] = courts.get(i);
	}

	public List<CourtTime> CourtTimesByLatest(Match m, int matchMinutes, Vector<Match> schedule)
	{
		int matchMs = matchMinutes * c_nMsPerMinute;
		List<CourtTime> comfortTimes = CourtTimes(m, matchMs, schedule, AvailabilityType.Comfort);
		List<CourtTime> allTimes = CourtTimes(m, matchMs, schedule, AvailabilityType.All);
		Collections.sort(comfortTimes, LatestCourtTime.getInstance());
		Collections.sort(allTimes, LatestCourtTime.getInstance());

		List<CourtTime> all = comfortTimes;
		for (CourtTime ct : allTimes)
			if (!AlreadyRepresented(ct, all))
				all.add(ct);
		return all;
	}

	private boolean AlreadyRepresented(CourtTime ct, List<CourtTime> times)
	{
		for (CourtTime time : times)
			if (time.toString().equals(ct.toString()))
				return true;

		return false;
	}

	private List<CourtTime> CourtTimes(Match m, int matchMs, Vector<Match> schedule, AvailabilityType type)
	{
		List<CourtTime> times = new LinkedList<CourtTime>();
		for (Court c : m_courts)
		{
			for (TimeSpan span : m.Availability(c, schedule, type))
			{
				Timestamp startTime = span.getStart();
				Timestamp endTime = new Timestamp(startTime.getTime() + matchMs);
				while (!endTime.after(span.getEnd()))
				{
					times.add(new CourtTime(c, new TimeSpan(startTime, endTime)));
					startTime = endTime;
					endTime = new Timestamp(startTime.getTime() + matchMs);
				}
			}
		}
		return times;
	}

	private Court[] m_courts;

	private static final int c_nMsPerMinute = 60000;
}
