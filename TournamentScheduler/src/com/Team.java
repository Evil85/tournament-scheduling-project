package com;

import java.util.SortedSet;
import java.util.Vector;
import java.util.List;

// Represents a team with any number of players in a tournament.
public class Team implements TimeConstraint {

	public Team(int id, String name, Player... players) {
		m_nId = id;
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

	public Team(int id, String name, List<Player> players) {
		m_nId = id;
		m_strName = name;
		m_availability = SchedulingUtil.IntersectAvailability(players.toArray(new Player[0]));
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

	public int Id()
	{
		return m_nId;
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
	private int m_nId;

}
