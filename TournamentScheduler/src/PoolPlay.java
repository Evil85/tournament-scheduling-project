import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class PoolPlay extends Division {

	public PoolPlay(int id, String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		super(id, name, minutesPerMatch, courts, players);
		SetupMatches();
	}
	
	public PoolPlay(int id, String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		super(id, name, minutesPerMatch, courts, teams);
		SetupMatches();
	}
	
	protected void SetupMatches()
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
		Match finalMatch = new Match(String.format("%s final match", m_strName), this, finalistA, finalistB);
		m_unscheduled.add(finalMatch);
		
		Team winnerRoundA = new Team("Winner A", firstRoundPlayers);
		Team runnerRoundA = new Team("Runner-up A", firstRoundPlayers);
		Team winnerRoundB = new Team("Winner B", secondRoundPlayers);
		Team runnerRoundB = new Team("Runner-up B", secondRoundPlayers);
		Match semi1 = new Match(String.format("%s semifinal match 1", m_strName), this, winnerRoundA, runnerRoundA);
		m_unscheduled.add(semi1);
		finalMatch.AddParent(semi1);
		Match semi2 = new Match(String.format("%s semifinal match 2", m_strName), this, winnerRoundB, runnerRoundB);
		m_unscheduled.add(semi2);
		finalMatch.AddParent(semi2);
		
		for (int i = 0; i < teams.length / 2; i++)
		{
			for (int j = i + 1; j < teams.length / 2; j++)
			{
				Match m = new Match(String.format("%s round A match %d", m_strName, i + j), this, teams[i], teams[j]);
				m_unscheduled.add(m);
				semi1.AddParent(m);
			}
		}
		
		for (int i = teams.length / 2; i < teams.length; i++)
		{
			for (int j = i + 1; j < teams.length; j++)
			{
				Match m = new Match(String.format("%s round B match %d", m_strName, i + j - (teams.length / 2) * 2), this, teams[i], teams[j]);
				m_unscheduled.add(m);
				semi2.AddParent(m);
			}
		}
	}
	
}
