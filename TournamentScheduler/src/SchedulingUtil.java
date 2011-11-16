import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;


public class SchedulingUtil {

	private static SchedulingUtil instance = null;
	
	protected SchedulingUtil() {}
	
	public static SchedulingUtil getInstance()
	{
		if (instance == null)
			instance = new SchedulingUtil();
		
		return instance;
	}
	
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
						return intersection;
					else
						currentOther = otherIterator.next();
				}
			}
		}
		
		return intersection;
	}
	
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
	
	public static SortedSet<TimeSpan> MatchAvailability(Match m, Court c, Vector<Match> previouslyScheduled)
	{
		Vector<TimeConstraint> constraints = new Vector<TimeConstraint>(m.Players());
		constraints.add(c);
		SortedSet<TimeSpan> availableTimes = IntersectAvailability(constraints.toArray(new TimeConstraint[0]));
		
		// Truncate the available times for any Matches for which this Match is based on the outcome.
		for (Match parent : m.Parents())
			Truncate(availableTimes, TruncateSide.Start, parent);
			
		for(Match other : previouslyScheduled)
		{
			TimeSpan otherTime = other.Time();
			if (otherTime != null)
			{
				// If the other match is dependent on the outcome of this match, truncate the available times.
				if (other.Parents().contains(m))
					Truncate(availableTimes, TruncateSide.End, other);
				// If the other match shares players with this one and is at a different Venue, Exclude a window of time for travel.
				if (m.SharesPlayers(other) && c.Venue() != other.Court().Venue())
				{
					Timestamp travelTimeStart = new Timestamp(otherTime.getStart().getTime() - c_nMsTravelTime);
					Timestamp travelTimeEnd = new Timestamp(otherTime.getEnd().getTime() + c_nMsTravelTime);
					TimeSpan travelTimeWindow = new TimeSpan(travelTimeStart, travelTimeEnd);
					Exclude(availableTimes, travelTimeWindow);
				}
			}
		}
		
		return availableTimes;
	}
		
	private static void Exclude(SortedSet<TimeSpan> set, TimeSpan exclude)
	{
		Collection<TimeSpan> removalList = new Vector<TimeSpan>();
		Collection<TimeSpan> additionList = new Vector<TimeSpan>();
		
		for (TimeSpan originalSpan : set)
		{
			if(originalSpan.Overlaps(exclude))
			{
				removalList.add(originalSpan);
				if (originalSpan.getStart().before(exclude.getStart()))
					additionList.add(new TimeSpan(originalSpan.getStart(), exclude.getStart()));
				if (originalSpan.getEnd().after(exclude.getEnd()))
					additionList.add(new TimeSpan(exclude.getEnd(), originalSpan.getEnd()));
			}
		}
		
		set.removeAll(removalList);
		set.addAll(additionList);
	}
	
	private static void Truncate(SortedSet<TimeSpan> set, TruncateSide side, Match m)
	{
		TimeSpan scheduledFor = m.Time();
		if (scheduledFor != null)
		{
			if (side == TruncateSide.End)
				Exclude(set, new TimeSpan(scheduledFor.getStart(), set.last().getEnd()));
			else
				Exclude(set, new TimeSpan(scheduledFor.getEnd(), set.first().getStart()));
		}
	}
	
	private static enum TruncateSide { Start, End }
	
	// 3,600,000 ms = 1 hour;
	private static final int c_nMsTravelTime = 3600000;
	
}
