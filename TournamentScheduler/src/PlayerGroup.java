import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;


public class PlayerGroup extends Overlappable {
	
	public PlayerGroup(String name, SortedSet<TimeSpan> availability) {
		super(name, availability);
		Player p = new Player(String.format("%s (player)", name), new ConcurrentSkipListSet<TimeSpan>(availability));
		SortedSet<Player> players = new ConcurrentSkipListSet<Player>();
		players.add(p);
		setPlayers(players);
	}
	
	public PlayerGroup(String name, Player firstPlayer, Player... additionalPlayers) {
		super(name, firstPlayer, additionalPlayers);
		SortedSet<Player> players = new ConcurrentSkipListSet<Player>();
		players.add(firstPlayer);
		for (Player p : additionalPlayers)
			players.add(p);
				
		setPlayers(players);
	}
	
	public SortedSet<Player> getPlayers() {
		return m_players;
	}

	public void setPlayers(SortedSet<Player> m_players) {
		this.m_players = m_players;
	}

	private SortedSet<Player> m_players;
	
}
