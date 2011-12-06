package com;

import java.util.Comparator;

// Compares CourtTimes, with latest first.
public class LatestCourtTime implements Comparator<CourtTime> {

	private static LatestCourtTime instance = null;

	protected LatestCourtTime() {}

	public static LatestCourtTime getInstance()
	{
		if (instance == null)
			instance = new LatestCourtTime();

		return instance;
	}

	public int compare(CourtTime first, CourtTime second)
	{
		return second.m_time.getStart().compareTo(first.m_time.getStart());
	}

}
