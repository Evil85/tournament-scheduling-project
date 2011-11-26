import java.util.LinkedList;


public class RoundRobin extends Division {

	public RoundRobin(int id, String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		super(id, name, minutesPerMatch, courts, players);
		SetupMatches();
	}
	
	public RoundRobin(int id, String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		super(id, name, minutesPerMatch, courts, teams);
		SetupMatches();
	}
	
	protected void SetupMatches()
	{
		m_unscheduled = new LinkedList<Match>();
		Team[] teams = m_teams.toArray(new Team[0]);
		for (int i = 0; i < teams.length; i++)
			for (int j = i + 1; j < teams.length; j++)
				m_unscheduled.add(new Match(String.format("%s match %d", m_strName, i + j), this, teams[i], teams[j]));
	}

}
