import java.lang.Comparable;
import java.util.SortedSet;

public class Overlappable implements Comparable<Overlappable> {
 
	/**
	 * Represents an entity with a name and a set of TimeSpans during which it is available.
	 * @param name
	 * @param availability
	 */
	public Overlappable(String name, SortedSet<TimeSpan> availability)
	{
		m_name = name;
		m_availability = availability;
		m_nAvailableMinutes = 0;
		for (TimeSpan span : m_availability)
			m_nAvailableMinutes += span.m_nMinutes;
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
        return m_name == other.m_name &&
        		m_availability.equals(other.m_availability);
    }

    public int hashCode()
    {
        int hash = 17;
        hash = hash * 31 + m_name.hashCode();
        hash = hash * 31 + m_availability.hashCode();
        return hash;
    }
	
    /**
     * Overlappables are sorted first by the number of available minutes and then by name.
     * Note that it is possible for compareTo() to be inconsistent with equals() if the two names are the same.
     */
	public int compareTo(Overlappable o) {
		int nDifference = m_nAvailableMinutes - o.m_nAvailableMinutes;
        if (nDifference != 0)
            return nDifference;
        else
            return m_name.compareTo(o.m_name);
	}
	
	public String toString()
	{
		return String.format("%s : %s", m_name, m_availability);
	}
	
	public String m_name;
	public SortedSet<TimeSpan> m_availability;
	public int m_nAvailableMinutes;

}
