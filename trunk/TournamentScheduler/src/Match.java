import java.sql.Timestamp;
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
		m_parents = new Vector<Match>();
	}
	
	public void AddParent(Match parent)
	{
		m_parents.add(parent);
	}
	
	public Vector<Match> Parents()
	{
		return m_parents;
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
		
		for (Player p : m_players)
			p.RemoveTime(m_time);
		m_court.RemoveTime(m_time);
	}
	
	public Boolean IsIdeal(Vector<Match> context)
	{
		for (Match other : context)
		{
			if (SharesPlayers(other))
			{
				Timestamp comfortStart = new Timestamp(m_time.getStart().getTime() - c_nMsComfortWindow);
				Timestamp comfortEnd = new Timestamp(m_time.getEnd().getTime() + c_nMsComfortWindow);
				TimeSpan comfortWindow = new TimeSpan(comfortStart, comfortEnd);
				if (comfortWindow.Overlaps(other.Time()))
					return false;
			}
		}
		
		return true;
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
	private Vector<Match> m_parents;
	
	// 1,800,000 ms = 30 minutes;
	private static final int c_nMsComfortWindow = 1800000;
	
}
