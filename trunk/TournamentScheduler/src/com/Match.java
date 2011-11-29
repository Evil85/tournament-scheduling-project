package com;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

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

}
