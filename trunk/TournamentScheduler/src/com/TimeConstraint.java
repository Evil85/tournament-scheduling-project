package com;

import java.util.SortedSet;

// An entity with a set of available times.
public interface TimeConstraint {

	public SortedSet<TimeSpan> Availability();
	public int AvailableMinutes();
	public String Name();

}
