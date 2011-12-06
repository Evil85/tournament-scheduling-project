package com;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

// Provides operations for sets of times.
public class SchedulingUtil {

	private static SchedulingUtil instance = null;

	protected SchedulingUtil() {}

	public static SchedulingUtil getInstance()
	{
		if (instance == null)
			instance = new SchedulingUtil();

		return instance;
	}

	// Returns the intersect of the available times (sets of times) of the specified TimeConstrains.
	public static SortedSet<TimeSpan> IntersectAvailability(TimeConstraint... constraints)
	{
		SortedSet<TimeSpan> availableTimes = new ConcurrentSkipListSet<TimeSpan>();

		if (constraints.length != 0)
		{
			availableTimes.addAll(constraints[0].Availability());
			for (int i = 1; i < constraints.length; i++)
				availableTimes = IntersectAvailability(availableTimes, constraints[i].Availability());
		}

		return availableTimes;
	}

	// Returns the intersect of two sets of times.
	public static SortedSet<TimeSpan> IntersectAvailability(SortedSet<TimeSpan> first, SortedSet<TimeSpan> second)
	{
		SortedSet<TimeSpan> intersection = new TreeSet<TimeSpan>();

		Iterator<TimeSpan> otherIterator = second.iterator();
		if (otherIterator.hasNext())
		{
			TimeSpan currentOther = otherIterator.next();
			for (TimeSpan originalSpan : first)
			{
				while (!currentOther.getEnd().after(originalSpan.getStart()))
					if (!otherIterator.hasNext())
						return intersection;
					else
						currentOther = otherIterator.next();

				while (currentOther.getStart().before(originalSpan.getEnd()))
				{
					if (currentOther.getStart().after(originalSpan.getStart()))
					{
						if (currentOther.getEnd().before(originalSpan.getEnd()))
							intersection.add(new TimeSpan(currentOther.getStart(), currentOther.getEnd()));
						else
							intersection.add(new TimeSpan(currentOther.getStart(), originalSpan.getEnd()));

					}
					else if (currentOther.getEnd().before(originalSpan.getEnd()))
					{
						intersection.add(new TimeSpan(originalSpan.getStart(), currentOther.getEnd()));
					}
					else
					{
						intersection.add(originalSpan);
					}

					if (!otherIterator.hasNext())
						break;
					else
						currentOther = otherIterator.next();
				}
			}
		}

		return intersection;
	}

	// Adds the specified TimeSpan to the specified set of times.
	public static void AddAvailability(SortedSet<TimeSpan> set, TimeSpan add)
	{
		Timestamp start = add.getStart();
		Timestamp end = add.getEnd();
		Vector<TimeSpan> removalList = new Vector<TimeSpan>();

		for (TimeSpan old : set)
		{
			if (old.getStart().after(end))
				break;

			if (!old.getEnd().before(start) && old.getStart().before(start))
			{
				start = old.getStart();
				removalList.add(old);
			}

			if (!old.getStart().after(end) && !old.getEnd().before(end))
			{
				end = old.getEnd();
				removalList.add(old);
			}
		}

		set.removeAll(removalList);
		set.add(new TimeSpan(start, end));
	}

	// Removes the specified TimeSpan from the specified set of times.
	public static void RemoveAvailability(SortedSet<TimeSpan> set, TimeSpan remove)
	{
		Timestamp start = remove.getStart();
		Timestamp end = remove.getEnd();
		Vector<TimeSpan> removalList = new Vector<TimeSpan>();
		Vector<TimeSpan> additionList = new Vector<TimeSpan>();

		for (TimeSpan old : set)
		{
			Timestamp oldStart = old.getStart();
			Timestamp oldEnd = old.getEnd();
			if (oldStart.after(end))
				break;

			if(old.Overlaps(remove))
			{
				removalList.add(old);
				if (oldStart.before(start))
					additionList.add(new TimeSpan(oldStart, start));
				if (oldEnd.after(end))
					additionList.add(new TimeSpan(end, oldEnd));
			}
		}

		set.removeAll(removalList);
		set.addAll(additionList);
	}

	// Truncates the specified set of times at the specified side of the specified boundary.
	public static void Truncate(SortedSet<TimeSpan> set, TruncateSide side, Timestamp boundary)
	{
		if (!set.isEmpty())
		{
			if (side == TruncateSide.End)
			{
				Timestamp end = set.last().getEnd();
				if (boundary.before(end))
					RemoveAvailability(set, new TimeSpan(boundary, end));
			}
			else
			{
				Timestamp start = set.first().getStart();
				if (start.before(boundary))
					RemoveAvailability(set, new TimeSpan(start, boundary));
			}
		}
	}

	public static enum TruncateSide { Start, End }

}
