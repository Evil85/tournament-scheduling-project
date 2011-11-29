package com;

import java.util.SortedSet;


public interface TimeConstraint {

	public SortedSet<TimeSpan> Availability();
	public int AvailableMinutes();
	public String Name();

}
