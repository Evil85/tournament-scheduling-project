package com;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

// Represents a division in a tournament. Creates and schedules the matches for that division.
public abstract class Division {

	public Division(int id, String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		m_nId = id;
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new LinkedList<Team>();
		for (Player p : players)
			m_teams.add(new Team(p.Name(), p));
		SortTeams();
	}

	public Division(int id, String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		m_nId = id;
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new LinkedList<Team>();
		for (Team t : teams)
			m_teams.add(t);
		SortTeams();
	}

	public int Id()
	{
		return m_nId;
	}

	public int MinutesPerMatch()
	{
		return m_nMinutesPerMatch;
	}

	public String Name()
	{
		return m_strName;
	}

	// Links this division to another (or itself) in a loop. This must be done before scheduling.
	public void LinkTo(Division next)
	{
		m_nextDivision = next;
	}

	// Given the existing schedule, try to schedule the remaining matches in this tournament, adding them to the schedule.
	public boolean TrySchedule(Vector<Match> schedule)
	{
		ResetScheduledCount(this);
		return TrySchedule(schedule, null);
	}

	protected abstract void SetupMatches();

	private void SortTeams()
	{
		Collections.sort(m_teams, MostRestrictiveTeam.getInstance());
	}

	private void ResetScheduledCount(Division firstReset)
	{
		m_nScheduled = 0;

		if (m_nextDivision != firstReset)
			ResetScheduledCount(firstReset);
	}

	private boolean TrySchedule(Vector<Match> schedule, Division firstFinished)
	{
		if (m_unscheduled.size() != 0)
		{
			while(m_unscheduled.size() != 0)
			{
				boolean bSuccess = false;
				Match m = m_unscheduled.remove(0);
				m_nScheduled++;
				if (m.Name().startsWith("X semi"))
					System.out.println();

				List<CourtTime> times = m_courts.CourtTimesByLatest(m, m_nMinutesPerMatch, schedule);

				for (CourtTime ct : times)
				{
					m.Schedule(ct);
					schedule.add(m);

					bSuccess = m_nextDivision.TrySchedule(schedule, null);
					if (!bSuccess)
						schedule.remove(m);
					else
						break;
				}

				if (bSuccess)
				{
					return true;
				}
				else
				{
					m.Unschedule();
					m_unscheduled.add(0, m);
					m_nScheduled--;

					if (m_nScheduled != 0)
						return false;

					m_teams.remove(0);
					SetupMatches();
				}
			}
			return true;
		}
		else if (m_nextDivision != firstFinished)
		{
			return m_nextDivision.TrySchedule(schedule, firstFinished == null ? this : firstFinished);
		}
		else
		{
			return true;
		}
	}

	private int m_nMinutesPerMatch;
	private int m_nId;
	private int m_nScheduled;
	private CourtManager m_courts;
	private Division m_nextDivision;
	protected String m_strName;
	protected List<Team> m_teams;
	protected List<Match> m_unscheduled;

}
