import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;



public class CourtManager {

	public CourtManager(Court... courts)
	{
		m_courts = courts;
	}
	
	public Boolean TryScheduleLatest(Match m, int matchMinutes, Match... previouslyScheduled)
	{
		CourtComparator comp = new CourtComparator(matchMinutes, m, previouslyScheduled);
		SortedSet<Court> set = new ConcurrentSkipListSet<Court>(comp);
		for (Court c : m_courts)
			set.add(c);
		
		Court latestCourt = set.first();
		Timestamp latestEnd = null;
		for (TimeSpan span : latestCourt.getAvailability())
			if (span.getMinutes() >= matchMinutes)
				latestEnd = span.getEnd();
				
		if (latestEnd != null)
			return m.Schedule(latestCourt, new TimeSpan(matchMinutes, latestEnd));
		else
			return false;
	}
	
	private Court[] m_courts;
}
