import java.util.SortedSet;


public class Player extends Overlappable {

	public Player(String name, SortedSet<TimeSpan> availability) {
		super(name, availability);
	}
	
}
