import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;


public class Player implements TimeConstraint {

	public Player(String name, TimeSpan... availability) {
		m_strName = name;
		m_teams = new Vector<Team>();
		m_availability = new TreeSet<TimeSpan>();
		if (availability.length != 0)
			m_availability.add(availability[0]);
		for (int i = 1; i < availability.length; i++)
			SchedulingUtil.AddAvailability(m_availability, availability[i]);
		UpdateAvailabilityMinutes();
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
	
	public void Enroll(Team t)
	{
		m_teams.add(t);
	}
	
	private void UpdateAvailabilityMinutes()
	{
		m_nAvailableMinutes = 0;
		for (TimeSpan span : m_availability)
			m_nAvailableMinutes += span.getMinutes();
	}
	
	private String m_strName;
	private SortedSet<TimeSpan> m_availability;
	private int m_nAvailableMinutes;
	private Vector<Team> m_teams;
	
}