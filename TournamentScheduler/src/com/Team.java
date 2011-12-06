package com;

import java.util.SortedSet;
import java.util.Vector;

// Represents a team with any number of players in a tournament.
public class Team implements TimeConstraint {

	public Team(String name, Player... players) {
		m_strName = name;
		m_availability = SchedulingUtil.IntersectAvailability(players);
		m_players = new Vector<Player>();
		for (Player p : players)
		{
			m_players.add(p);
			p.Enroll(this);
		}
		m_nAvailableMinutes = 0;
		for (TimeSpan span : m_availability)
			m_nAvailableMinutes += span.getMinutes();
	}

	public SortedSet<TimeSpan> Availability()
	{
		return m_availability;
	}

	public int AvailableMinutes()
	{
		return m_nAvailableMinutes;
	}

	public String Name()
	{
		return m_strName;
	}

	public Vector<Player> Players()
	{
		return m_players;
	}

	public String toString()
	{
		return String.format("%s : %s", m_strName, Availability());
	}

	private String m_strName;
	private SortedSet<TimeSpan> m_availability;
	private int m_nAvailableMinutes;
	private Vector<Player> m_players;

}
