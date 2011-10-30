import java.util.LinkedList;
import java.util.List;


public class RoundRobin extends Division {

	public RoundRobin(String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		super(name, minutesPerMatch, courts, players);
		Setup();
	}
	
	public RoundRobin(String name, int minutesPerMatch, CourtManager courts, PlayerGroup... teams)
	{
		super(name, minutesPerMatch, courts, teams);
		Setup();
	}
	
	public Boolean FullyScheduled()
	{
		return m_unscheduled.isEmpty();
	}
	
	public Match TryScheduleMatch(Match... previouslyScheduled)
	{
		Match m = m_unscheduled.get(0);
		if (m_courts.TryScheduleLatest(m, getMinutesPerMatch()))
		{
			m_unscheduled.remove(0);
			m_scheduled.add(m);
			return m;
		}
		else
		{
			return null;
		}
	}
	
	private void Setup()
	{
		m_unscheduled = new LinkedList<Match>();
		m_scheduled = new LinkedList<Match>();
		PlayerGroup[] teams = m_teams.toArray(new PlayerGroup[0]);
		for (int i = 0; i < teams.length; i++)
			for (int j = i + 1; j < teams.length; j++)
				m_unscheduled.add(new Match(String.format("%s match %d", m_strName, i + j), teams[i], teams[j]));
	}
	
	private List<Match> m_unscheduled;
	private List<Match> m_scheduled;

}
