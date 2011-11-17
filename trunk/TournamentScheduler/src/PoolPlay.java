import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;


public class PoolPlay implements Division {

	public PoolPlay(String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Player p : players)
			m_teams.add(new Team(p.Name(), p));
		SetupMatches();
	}
	
	public PoolPlay(String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Team t : teams)
			m_teams.add(t);
		SetupMatches();
	}
	
	public void LinkTo(Division next)
	{
		m_nextDivision = next;
	}
	
	public boolean TrySchedule(Vector<Match> schedule)
	{
		if (m_nextDivision == null)
			throw new IllegalStateException("Tried to schedule an unlinked division.");
		
		if (m_unscheduled.size() != 0)
		{
			Match m = m_unscheduled.remove(0);
			
			List<CourtTime> times = m_courts.CourtTimesByLatest(m, m_nMinutesPerMatch, schedule);
			
			for (CourtTime ct : times)
			{
				m.Schedule(ct);
				schedule.add(m);
				
				if (m_nextDivision.TrySchedule(schedule))
					return true;
				else
					schedule.remove(m);
			}
			
			m.Unschedule();
			m_unscheduled.add(0, m);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public String Name()
	{
		return m_strName;
	}
	
	public int MinutesPerMatch()
	{
		return m_nMinutesPerMatch;
	}
	
	private void SetupMatches()
	{
		m_unscheduled = new LinkedList<Match>();
		Set<Player> firstRound = new HashSet<Player>();
		Set<Player> secondRound = new HashSet<Player>();
		Team[] teams = m_teams.toArray(new Team[0]);
		for (int i = 0; i < teams.length / 2; i++)
			firstRound.addAll(teams[i].Players());
		for (int i = teams.length / 2; i < teams.length; i++)
			secondRound.addAll(teams[i].Players());
		Player[] firstRoundPlayers = firstRound.toArray(new Player[0]);
		Player[] secondRoundPlayers = secondRound.toArray(new Player[0]);
		
		Team finalistA =  new Team("Finalist A", firstRoundPlayers);
		Team finalistB =  new Team("Finalist B", secondRoundPlayers);
		Match finalMatch = new Match(String.format("%s final match", m_strName), finalistA, finalistB);
		m_unscheduled.add(finalMatch);
		
		Team winnerRoundA = new Team("Winner A", firstRoundPlayers);
		Team runnerRoundA = new Team("Runner-up A", firstRoundPlayers);
		Team winnerRoundB = new Team("Winner B", secondRoundPlayers);
		Team runnerRoundB = new Team("Runner-up B", secondRoundPlayers);
		Match semi1 = new Match(String.format("%s semifinal match 1", m_strName), winnerRoundA, runnerRoundA);
		m_unscheduled.add(semi1);
		finalMatch.AddParent(semi1);
		Match semi2 = new Match(String.format("%s semifinal match 2", m_strName), winnerRoundB, runnerRoundB);
		m_unscheduled.add(semi2);
		finalMatch.AddParent(semi2);
		
		for (int i = 0; i < teams.length / 2; i++)
		{
			for (int j = i + 1; j < teams.length / 2; j++)
			{
				Match m = new Match(String.format("%s round A match %d", m_strName, i + j), teams[i], teams[j]);
				m_unscheduled.add(m);
				semi1.AddParent(m);
			}
		}
		
		for (int i = teams.length / 2; i < teams.length; i++)
		{
			for (int j = i + 1; j < teams.length; j++)
			{
				Match m = new Match(String.format("%s round B match %d", m_strName, i + j - (teams.length / 2) * 2), teams[i], teams[j]);
				m_unscheduled.add(m);
				semi2.AddParent(m);
			}
		}
	}
	
	private String m_strName;
	private List<Match> m_unscheduled;
	private int m_nMinutesPerMatch;
	private Vector<Team> m_teams;
	private CourtManager m_courts;
	private Division m_nextDivision = null;
	
}
