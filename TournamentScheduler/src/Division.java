import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;


public abstract class Division {

	public Division(String name, int minutesPerMatch, CourtManager courts, Player... players)
	{
		m_strName = name;
		setMinutesPerMatch(minutesPerMatch);
		setMatchType(MatchType.Singles);
		m_courts = courts;
		m_teams = new ConcurrentSkipListSet<PlayerGroup>();
		for (Player p : players)
			m_teams.add(new PlayerGroup(p));
	}
	
	public Division(String name, int minutesPerMatch, CourtManager courts, PlayerGroup... teams)
	{
		m_strName = name;
		setMinutesPerMatch(minutesPerMatch);
		setMatchType(MatchType.Doubles);
		m_courts = courts;
		m_teams = new ConcurrentSkipListSet<PlayerGroup>();
		for (PlayerGroup t : teams)
			m_teams.add(t);
	}
	
	public abstract Boolean FullyScheduled();
	public abstract Match TryScheduleMatch(Match... previouslyScheduled);
	
	public int getMinutesPerMatch(){
		return m_nMinutesPerMatch;
	}
	
	private void setMinutesPerMatch(int m_nMinutesPerMatch){
		this.m_nMinutesPerMatch = m_nMinutesPerMatch;
	}
				
	public MatchType getMatchType(){
		return m_matchType;
	}
	
	private void setMatchType(MatchType m_matchType){
		this.m_matchType = m_matchType;
	}
	
	protected String m_strName;
	private int m_nMinutesPerMatch;
	private MatchType m_matchType;
	protected SortedSet<PlayerGroup> m_teams;
	protected CourtManager m_courts;
	
}
