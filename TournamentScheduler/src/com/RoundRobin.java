package com;

import java.util.LinkedList;
import java.util.List;

// A division in which each team plays each other team.
public class RoundRobin extends Division {

	public RoundRobin(int id, String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		super(id, name, minutesPerMatch, courts, players);
		SetupMatches();
	}

	public RoundRobin(int id, String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		super(id, name, minutesPerMatch, courts, teams);
		SetupMatches();
	}

	public RoundRobin(int id, String name, int minutesPerMatch, CourtManager courts, List<Team> teams)
	{
		super(id, name, minutesPerMatch, courts, teams);
		SetupMatches();
	}

	protected void SetupMatches()
	{
		m_unscheduled = new LinkedList<Match>();
		if (m_teams.size() < 3)
			return;

		int nTeams = m_teams.size();
		int nMatchId = (nTeams * (nTeams - 1)) / 2;

		Team[] teams = m_teams.toArray(new Team[0]);
		for (int i = 0; i < teams.length; i++)
			for (int j = i + 1; j < teams.length; j++)
				m_unscheduled.add(new Match(--nMatchId, String.format("%s match %d", m_strName, i + j), this, teams[i], teams[j]));
	}

}
