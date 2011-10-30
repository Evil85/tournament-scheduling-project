import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class Match extends PlayerGroup {
	
	/**
	 * Represents a match between two groups at a court. Can be later scheduled.
	 * If a specified group is another Match, this match will be considered to be dependent on the outcome of the specified match.
	 * @param name
	 * @param court
	 * @param group1
	 * @param group2
	 */
	public Match(String name, PlayerGroup group1, PlayerGroup group2)
	{
		super(name, group1, group2);
		setGroup1(group1);
		setGroup2(group2);
		SortedSet<Player> players = new ConcurrentSkipListSet<Player>(group1.getPlayers());
		players.addAll(group2.getPlayers());
		setPlayers(players);
		setTime(null);
	}
	
	public String toString()
    {
		if (getTime() == null)
			return String.format("%s: %s vs. %s", getName(), getGroup1().getName(), getGroup2().getName());
		else
			return String.format("%s: %s vs. %s at %s, %s", getName(), getGroup1().getName(), getGroup2().getName(), getCourt().getName(), getTime());
    }
	
	public Boolean Schedule(Court court, TimeSpan time)
	{
		if (time.Within(getAvailability()) && time.Within(court.getAvailability()))
		{
			setCourt(court);
			setTime(time);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public SortedSet<TimeSpan> getAvailability(Court c, Match... previouslyScheduled)
	{
		Overlappable withCourt = new Overlappable("temp", this, c);
		SortedSet<TimeSpan> availableTimes = new ConcurrentSkipListSet<TimeSpan>(withCourt.getAvailability());
		
		// Truncate the available times for any Matches for which this Match is based on the outcome.
		Boolean bMatch1 = getGroup1() instanceof Match;
		Boolean bMatch2 = getGroup2() instanceof Match;
		if (bMatch1 && bMatch2)
			Truncate(availableTimes, TruncateSide.Start, (Match)getGroup1(), (Match)getGroup2());
		else if (bMatch1)
			Truncate(availableTimes, TruncateSide.Start, (Match)getGroup1());
		else if (bMatch2)
			Truncate(availableTimes, TruncateSide.Start, (Match)getGroup2());
			
		for(Match other : previouslyScheduled)
		{
			TimeSpan otherTime = other.getTime();
			if (otherTime != null)
			{
				// If the other match is dependent on the outcome of this match, truncate the available times.
				if (other.getGroup1() == this || other.getGroup2() == this)
					Truncate(availableTimes, TruncateSide.End, other);
				// If the other match shares one or more players (or court) with this one, exclude the other match's time.
				if (c == other.getCourt() || SharesPlayers(other))
					Exclude(availableTimes, other.getTime());
			}
		}
		
		return availableTimes;
	}

	public Court getCourt() {
		return m_court;
	}

	private void setCourt(Court m_court) {
		this.m_court = m_court;
	}

	public PlayerGroup getGroup1() {
		return m_group1;
	}

	private void setGroup1(PlayerGroup m_group1) {
		this.m_group1 = m_group1;
	}

	public PlayerGroup getGroup2() {
		return m_group2;
	}

	private void setGroup2(PlayerGroup m_group2) {
		this.m_group2 = m_group2;
	}
	
	public SortedSet<Player> getPlayers() {
		return m_players;
	}

	private void setPlayers(SortedSet<Player> m_players) {
		this.m_players = m_players;
	}
	
	public TimeSpan getTime() {
		return m_time;
	}
	
	private void setTime(TimeSpan m_time) {
		this.m_time = m_time;
	}
	
	/**
	 * Determines whether or not the other Match shares any players with this one.
	 * @param other
	 * @return
	 */
	private Boolean SharesPlayers(Match other)
	{
		for (Player o : other.getPlayers())
		{
			if (m_players.contains(o))
				return true;
		}
		return false;
	}
	
	private static void Exclude(SortedSet<TimeSpan> set, TimeSpan exclude)
	{
		Collection<TimeSpan> removalList = new ArrayList<TimeSpan>();
		Collection<TimeSpan> additionList = new ArrayList<TimeSpan>();
		
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
	
	private static void Truncate(SortedSet<TimeSpan> set, TruncateSide side, Match... matches)
	{
		for (Match m : matches)
		{
			TimeSpan scheduledFor = m.getTime();
			if (scheduledFor != null)
			{
				if (side == TruncateSide.End)
					Exclude(set, new TimeSpan(scheduledFor.getStart(), set.last().getEnd()));
				else
					Exclude(set, new TimeSpan(scheduledFor.getEnd(), set.first().getStart()));
			}
		}
	}

	private Court m_court;
	private PlayerGroup m_group1;
	private PlayerGroup m_group2;
	private SortedSet<Player> m_players;
	private TimeSpan m_time;
	
	private enum TruncateSide { Start, End }
	
}
