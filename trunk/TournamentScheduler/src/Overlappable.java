import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class Overlappable implements Comparable<Overlappable> {
 
	/**
	 * Represents an entity with a name and a set of TimeSpans during which it is available.
	 * @param name
	 * @param availability
	 */
	public Overlappable(String name, SortedSet<TimeSpan> availability)
	{
		setName(name);
		setAvailability(availability);
		CombineAdjacentTimes();
		setAvailableMinutes(0);
		for (TimeSpan span : getAvailability())
			setAvailableMinutes(getAvailableMinutes() + span.getMinutes());
	}
	
	public Overlappable(String name, Overlappable basis, Overlappable... others)
	{
		setName(name);
		setAvailability(new ConcurrentSkipListSet<TimeSpan>(basis.getAvailability()));
		for (Overlappable o : others)
			ConstrainAvailability(o.getAvailability());
		setAvailableMinutes(0);
		for (TimeSpan span : getAvailability())
			setAvailableMinutes(getAvailableMinutes() + span.getMinutes());
	}
	
	/**
	 * Two Overlaps are considered equal if the name and availability are the same.
	 */
    public boolean equals(Object obj)
    {
    	if (this == obj)
    		return true;
    	if (!(obj instanceof Overlappable) || obj == null)
    		return false;
    	
        Overlappable other = (Overlappable)obj;
        return getName() == other.getName() &&
        		getAvailability().equals(other.getAvailability());
    }

    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + getName().hashCode();
        hash = hash * 31 + getAvailability().hashCode();
        return hash;
    }
	
    /**
     * Overlappables are sorted first by the number of available minutes and then by name.
     * Note that it is possible for compareTo() to be inconsistent with equals() if the two names are the same.
     */
	public int compareTo(Overlappable o) {
		int nDifference = getAvailableMinutes() - o.getAvailableMinutes();
        if (nDifference != 0)
            return nDifference;
        else
            return getName().compareTo(o.getName());
	}
	
	public String toString()
	{
		return String.format("%s : %s", getName(), getAvailability());
	}
	
	public String getName() {
		return m_strName;
	}

	protected void setName(String m_strName) {
		this.m_strName = m_strName;
	}

	public SortedSet<TimeSpan> getAvailability() {
		return m_availability;
	}

	protected void setAvailability(SortedSet<TimeSpan> m_availability) {
		this.m_availability = m_availability;
	}

	public int getAvailableMinutes() {
		return m_nAvailableMinutes;
	}

	protected void setAvailableMinutes(int m_nAvailableMinutes) {
		this.m_nAvailableMinutes = m_nAvailableMinutes;
	}

	/**
	 * Constrain m_availability to its intersection with the specified set of TimeSpans.
	 * @param other
	 */
	private void ConstrainAvailability(SortedSet<TimeSpan> other)
	{
		Iterator<TimeSpan> otherIterator = other.iterator();
		TimeSpan currentOther = otherIterator.next();
		for (TimeSpan originalSpan : getAvailability())
		{
			while (!currentOther.getEnd().after(originalSpan.getStart()))
				if (!otherIterator.hasNext())
					return;
				else
					currentOther = otherIterator.next();
			
			getAvailability().remove(originalSpan);
			while (currentOther.getStart().before(originalSpan.getEnd()))
			{
				if (currentOther.getStart().after(originalSpan.getStart()))
				{
					if (currentOther.getStart().before(originalSpan.getEnd()))
						getAvailability().add(new TimeSpan(currentOther.getStart(), currentOther.getEnd()));
					else
						getAvailability().add(new TimeSpan(currentOther.getStart(), originalSpan.getEnd()));
						
				}
				else if (currentOther.getEnd().before(originalSpan.getEnd()))
				{
					getAvailability().add(new TimeSpan(originalSpan.getStart(), currentOther.getEnd()));
				}
				else
				{
					getAvailability().add(originalSpan);
				}
				
				if (!otherIterator.hasNext())
					return;
				else
					currentOther = otherIterator.next();
			}
		}
	}
	
	/**
	 * Combine any adjacent TimeSpans in m_availability.
	 */
	private void CombineAdjacentTimes()
	{
		Collection<TimeSpan> removalList = new ArrayList<TimeSpan>();
		Collection<TimeSpan> additionList = new ArrayList<TimeSpan>();
		
		Iterator<TimeSpan> tsIterator = getAvailability().iterator();
		TimeSpan current = tsIterator.next();
		while (tsIterator.hasNext())
		{
			TimeSpan previous = current;
			current = tsIterator.next();
			
			if (previous.getEnd() == current.getStart())
			{
				removalList.add(previous);
				removalList.add(current);
				// If more than 2 adjacent times exist, make sure we only add the most complete span back in.
				current = new TimeSpan(previous.getStart(), current.getEnd());
				additionList.add(current);
				additionList.remove(previous);
			}
		}
		
		getAvailability().removeAll(removalList);
		getAvailability().addAll(additionList);
	}
	
	private String m_strName;
	private SortedSet<TimeSpan> m_availability;
	private int m_nAvailableMinutes;

}
