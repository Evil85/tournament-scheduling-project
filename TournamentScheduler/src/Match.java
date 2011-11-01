import java.util.Vector;

public class Match {

	public Match(String name, Team team1, Team team2)
	{
		m_strName = name;
		m_team1 = team1;
		m_team2 = team2;
		m_players = new Vector<Player>(team1.Players());
		m_players.addAll(team2.Players());
		m_court = null;
		m_time = null;
	}
	
	public String toString()
    {
		if (m_time == null)
			return String.format("%s: %s vs. %s", m_strName, m_team1.Name(), m_team2.Name());
		else
			return String.format("%s: %s vs. %s at %s, %s", m_strName, m_team1.Name(), m_team2.Name(), m_court.Name(), m_time);
    }
	
	public void Schedule(Court court, TimeSpan time)
	{
		m_court = court;
		m_time = time;
	}
	
	public Boolean SharesPlayers(Match other)
	{
		for (Player o : other.Players())
		{
			if (m_players.contains(o))
				return true;
		}
		return false;
	}
	
	public TimeSpan Time()
	{
		return m_time;
	}
	
	public Court Court()
	{
		return m_court;
	}
	
	protected Vector<Player> Players()
	{
		return m_players;
	}

	private String m_strName;
	private Court m_court;
	private Team m_team1;
	private Team m_team2;
	private Vector<Player> m_players;
	private TimeSpan m_time;
	
}
