import java.sql.Timestamp;
import java.util.Comparator;


public class CourtComparator implements Comparator<Court> {
	
	public CourtComparator(int matchMinutes, Match m, Match... previouslyScheduled)
	{
		m_nMatchMinutes = matchMinutes;
		m_match = m;
		m_previouslyScheduled = previouslyScheduled;
	}
	
	public int compare(Court first, Court second)
	{
		Timestamp latestFirst = null;
		for (TimeSpan span : m_match.getAvailability(first, m_previouslyScheduled))
			if (span.getMinutes() >= m_nMatchMinutes)
					latestFirst = span.getEnd();
				
		Timestamp latestSecond = null;
		for (TimeSpan span : m_match.getAvailability(second, m_previouslyScheduled))
			if (span.getMinutes() >= m_nMatchMinutes)
				latestSecond = span.getEnd();
				
		if (latestFirst != null)
		{
			if (latestSecond != null)
			{
				int nRelation = -latestFirst.compareTo(latestSecond);
				if (nRelation != 0)
					return nRelation;
				else
					return first.compareTo(second);
			}
			else
			{
				return -1;
			}
		}
		else
		{
			if (latestSecond != null)
				return 1;
			else
				return 0;
		}
	}

	private int m_nMatchMinutes;
	private Match m_match;
	private Match[] m_previouslyScheduled;
	
}
