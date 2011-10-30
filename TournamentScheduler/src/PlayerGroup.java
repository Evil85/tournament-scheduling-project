import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;


public class PlayerGroup extends Overlappable {
	
	/** This constructor would only be necessary if you wanted to have groups where we don't really care about the players and so we make one up.
		public PlayerGroup(String name, SortedSet<TimeSpan> availability) {
		super(name, availability);
		Player p = new Player(String.format("%s (player)", name), new ConcurrentSkipListSet<TimeSpan>(availability));
		SortedSet<Player> players = new ConcurrentSkipListSet<Player>();
		players.add(p);
		setPlayers(players);
	}
	*/
	
	public PlayerGroup(Player p)
	{
		super(p.getName(), p);
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
	
	public PlayerGroup(String name, Overlappable basis, Overlappable... additional)
	{
		super(name, basis, additional);
		SortedSet<Player> players = new ConcurrentSkipListSet<Player>();
		if (basis instanceof PlayerGroup)
			players.addAll(((PlayerGroup)basis).getPlayers());
		for (Overlappable o : additional)
			if (o instanceof PlayerGroup)
				players.addAll(((PlayerGroup)o).getPlayers());
		
		setPlayers(players);
	}
	
	public SortedSet<Player> getPlayers() {
		return m_players;
	}

	private void setPlayers(SortedSet<Player> m_players) {
		this.m_players = m_players;
	}

	private SortedSet<Player> m_players;
	
}
