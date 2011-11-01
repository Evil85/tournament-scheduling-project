import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;



public class CourtManager {

	public CourtManager(Court... courts)
	{
		m_courts = courts;
	}
	
	public Boolean TryScheduleLatest(Match m, int matchMinutes, Vector<Match> schedule)
	{
		CourtComparator comp = new CourtComparator(matchMinutes, m, schedule);
		SortedSet<Court> set = new ConcurrentSkipListSet<Court>(comp);
		for (Court c : m_courts)
			set.add(c);
		
		Court latestCourt = set.first();
		Timestamp latestEnd = null;
		for (TimeSpan span : latestCourt.Availability())
			if (span.getMinutes() >= matchMinutes)
				latestEnd = span.getEnd();
				
		if (latestEnd != null)
		{
			m.Schedule(latestCourt, new TimeSpan(matchMinutes, latestEnd));
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private Court[] m_courts;
}
