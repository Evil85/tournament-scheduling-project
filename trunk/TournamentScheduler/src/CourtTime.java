
public class CourtTime {

	public CourtTime(Court c, TimeSpan t)
	{
		m_court = c;
		m_time = t;
	}
	
	public String toString()
	{
		return String.format("%s at %s", m_court.Name(), m_time);
	}
	
	public Court m_court;
	public TimeSpan m_time;
	
}
