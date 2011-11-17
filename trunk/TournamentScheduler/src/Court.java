import java.util.SortedSet;
import java.util.TreeSet;


public class Court implements TimeConstraint {

	public Court(String name, String venue, TimeSpan... availability) {
		m_strName = String.format("%s (%s)", name, venue);
		m_strVenue = venue;
		m_availability = new TreeSet<TimeSpan>();
		if (availability.length != 0)
			m_availability.add(availability[0]);
		for (int i = 1; i < availability.length; i++)
			SchedulingUtil.AddAvailability(m_availability, availability[i]);
		UpdateAvailabilityMinutes();
	}
	
	public String Venue() {
		return m_strVenue;
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
	
	public String toString()
	{
		return String.format("%s : %s", m_strName, m_availability);
	}
	
	private void UpdateAvailabilityMinutes()
	{
		m_nAvailableMinutes = 0;
		for (TimeSpan span : m_availability)
			m_nAvailableMinutes += span.getMinutes();
	}

	private String m_strName;
	private String m_strVenue;
	private SortedSet<TimeSpan> m_availability;
	private int m_nAvailableMinutes;

}
