package com;

import java.util.Vector;
import java.util.List;

// Represents a tournament with any number of divisions.
public class Tournament {

	public Tournament(int id, Division... divisions)
	{
		m_nId = id;
		m_divisions = divisions;

		Division previous = m_divisions[m_divisions.length - 1];
		for (int i = 0; i < m_divisions.length; i++)
		{
			previous.LinkTo(m_divisions[i]);
			previous = m_divisions[i];
		}
	}

	public Tournament(int id, List<Division> divisions)
	{
		m_nId = id;
		m_divisions = divisions.toArray(new Division[0]);

		Division previous = m_divisions[m_divisions.length - 1];
		for (int i = 0; i < m_divisions.length; i++)
		{
			previous.LinkTo(m_divisions[i]);
			previous = m_divisions[i];
		}
	}

	public Vector<Match> Schedule()
	{
		Vector<Match> schedule = new Vector<Match>();
		m_divisions[0].TrySchedule(schedule);

		return schedule;
	}

	public int Id()
	{
		return m_nId;
	}

	private Division[] m_divisions;
	private int m_nId;

}
