import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


public class RoundRobin implements Division {

	public RoundRobin(String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Player p : players)
			m_teams.add(new Team(p.Name(), p));
		SetupMatches();
	}
	
	public RoundRobin(String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Team t : teams)
			m_teams.add(t);
		SetupMatches();
	}
	
	public boolean TrySchedule(Vector<Match> schedule)
	{
		if (m_unscheduled.size() != 0)
		{
			Match m = m_unscheduled.get(0);
			if (m_courts.TryScheduleLatest(m, m_nMinutesPerMatch, schedule))
			{
				m_unscheduled.remove(0);
				schedule.add(m);
				return TrySchedule(schedule);
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	private void SetupMatches()
	{
		m_unscheduled = new LinkedList<Match>();
		Team[] teams = m_teams.toArray(new Team[0]);
		for (int i = 0; i < teams.length; i++)
			for (int j = i + 1; j < teams.length; j++)
				m_unscheduled.add(new Match(String.format("%s match %d", m_strName, i + j), teams[i], teams[j]));
	}
	
	private String m_strName;
	private List<Match> m_unscheduled;
	private int m_nMinutesPerMatch;
	private Vector<Team> m_teams;
	private CourtManager m_courts;

}
