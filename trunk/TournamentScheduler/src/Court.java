import java.util.SortedSet;


public class Court extends Overlappable {

	public Court(String name, SortedSet<TimeSpan> availability, String venue) {
		super(name, availability);
		setVenue(venue);
		setName(String.format("%s (%s)", getName(), getVenue()));
	}
	
	public String getVenue() {
		return m_strVenue;
	}

	private void setVenue(String m_strVenue) {
		this.m_strVenue = m_strVenue;
	}

	private String m_strVenue;

}
