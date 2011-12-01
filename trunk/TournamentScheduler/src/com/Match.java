package com;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import com.SchedulingUtil.TruncateSide;

public class Match {

	public Match(String name, Division division, Team team1, Team team2)
	{
		m_strName = name;
		m_division = division;
		m_team1 = team1;
		m_team2 = team2;
		m_players = new HashSet<Player>(team1.Players());
		m_players.addAll(team2.Players());
		m_court = null;
		m_time = null;
		m_parents = new Vector<Match>();
	}
	
	public SortedSet<TimeSpan> Availability(Court c, Vector<Match> previouslyScheduled, AvailabilityType type)
	{
		Vector<TimeConstraint> constraints = new Vector<TimeConstraint>(Players());
		constraints.add(c);
		SortedSet<TimeSpan> availableTimes = SchedulingUtil.IntersectAvailability(constraints.toArray(new TimeConstraint[0]));
		SortedSet<TimeSpan> discomfortTimes = new TreeSet<TimeSpan>();

		// Truncate the available times for any Matches for which this Match is based on the outcome.
		for (Match parent : Parents())
			if (parent.Time() != null)
				SchedulingUtil.Truncate(availableTimes, TruncateSide.Start, parent.Time().getEnd());

		for(Match other : previouslyScheduled)
		{
			TimeSpan otherTime = other.Time();
			if (otherTime != null)
			{
				// If the other match is dependent on the outcome of this match, truncate the available times.
				if (other.Parents().contains(this))
					if (other.Time() != null)
						SchedulingUtil.Truncate(availableTimes, TruncateSide.End, other.Time().getStart());

				// If the other match shares players with this one, exclude the appropriate time window.
				if (SharesPlayers(other))
				{
					if (c.Venue() != other.Court().Venue())
					{
						Timestamp travelTimeStart = new Timestamp(otherTime.getStart().getTime() - c_nMsTravelTime);
						Timestamp travelTimeEnd = new Timestamp(otherTime.getEnd().getTime() + c_nMsTravelTime);
						TimeSpan travelTimeWindow = new TimeSpan(travelTimeStart, travelTimeEnd);
						SchedulingUtil.RemoveAvailability(availableTimes, travelTimeWindow);
					}
					else
					{
						SchedulingUtil.RemoveAvailability(availableTimes, otherTime);
					}

					if (type != AvailabilityType.All)
					{
						Timestamp discomfortStart = new Timestamp(otherTime.getStart().getTime() - c_nMsComfortWindow);
						Timestamp discomfortEnd = new Timestamp(otherTime.getEnd().getTime() + c_nMsComfortWindow);
						SchedulingUtil.AddAvailability(discomfortTimes, new TimeSpan(discomfortStart, discomfortEnd));
					}
				}
				else if (c == other.Court())
				{
					SchedulingUtil.RemoveAvailability(availableTimes, otherTime);
				}
			}
		}

		switch (type)
		{
			case All:
				return availableTimes;
			case Comfort:
				for (TimeSpan span : discomfortTimes)
					SchedulingUtil.RemoveAvailability(availableTimes, span);
				return availableTimes;
			case Discomfort:
				return SchedulingUtil.IntersectAvailability(availableTimes, discomfortTimes);
			default:
				throw new UnsupportedOperationException();
		}
	}

	public void AddParent(Match parent)
	{
		m_parents.add(parent);
	}

	public Vector<Match> Parents()
	{
		return m_parents;
	}

	public String toString()
    {
		if (m_time == null)
			return String.format("%s: %s vs. %s", m_strName, m_team1.Name(), m_team2.Name());
		else
			return String.format("%s: %s vs. %s at %s, %s", m_strName, m_team1.Name(), m_team2.Name(), m_court.Name(), m_time);
    }

	public void Schedule(CourtTime ct)
	{
		m_court = ct.m_court;
		m_time = ct.m_time;
	}

	public void Unschedule()
	{
		m_court = null;
		m_time = null;
	}

	public Boolean SharesPlayers(Match other)
	{
		for (Player o : other.Players())
		{
			if (m_players.contains(o))
				return true;
		}
		return false;
	}

	public TimeSpan Time()
	{
		return m_time;
	}

	public Court Court()
	{
		return m_court;
	}

	public String Name()
	{
		return m_strName;
	}

	public Division Division()
	{
		return m_division;
	}

	protected Set<Player> Players()
	{
		return m_players;
	}

	private String m_strName;
	private Court m_court;
	private Division m_division;
	private Team m_team1;
	private Team m_team2;
	private Set<Player> m_players;
	private TimeSpan m_time;
	private Vector<Match> m_parents;
	
	// 3,600,000 ms = 1 hour;
	private static final int c_nMsTravelTime = 3600000;

	// 1,800,000 ms = 30 minutes;
	private static final int c_nMsComfortWindow = 1800000;

}
