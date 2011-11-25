import java.util.List;
import java.util.Vector;


public abstract class Division {
	
	public Division(int id, String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		m_nId = id;
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Player p : players)
			m_teams.add(new Team(p.Name(), p));
	}
	
	public Division(int id, String name, int minutesPerMatch, CourtManager courts, Team... teams)
	{
		m_nId = id;
		m_strName = name;
		m_nMinutesPerMatch = minutesPerMatch;
		m_courts = courts;
		m_teams = new Vector<Team>();
		for (Team t : teams)
			m_teams.add(t);
	}
	
	public int Id()
	{
		return m_nId;
	}
	
	public int MinutesPerMatch()
	{
		return m_nMinutesPerMatch;
	}
	
	public String Name()
	{
		return m_strName;
	}
	
	public void LinkTo(Division next)
	{
		m_nextDivision = next;
	}
	
	public boolean TrySchedule(Vector<Match> schedule)
	{
		return TrySchedule(schedule, null);
	}
	
	private boolean TrySchedule(Vector<Match> schedule, Division firstFinished)
	{
		if (m_unscheduled.size() != 0)
		{
			Match m = m_unscheduled.remove(0);
			
			List<CourtTime> times = m_courts.CourtTimesByLatest(m, m_nMinutesPerMatch, schedule);
			
			for (CourtTime ct : times)
			{
				m.Schedule(ct);
				schedule.add(m);
				
				if (m_nextDivision.TrySchedule(schedule, null))
					return true;
				else
					schedule.remove(m);
			}
			
			m.Unschedule();
			m_unscheduled.add(0, m);
			return false;
		}
		else if (m_nextDivision != firstFinished)
		{
			return m_nextDivision.TrySchedule(schedule, firstFinished == null ? this : firstFinished);
		}
		else
		{
			return true;
		}
	}
	
	private int m_nMinutesPerMatch;
	private CourtManager m_courts;
	private Division m_nextDivision;
	private int m_nId;
	protected String m_strName;
	protected Vector<Team> m_teams;
	protected List<Match> m_unscheduled;
	
}
