import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class PlayerGroupTest {

	@Test
	public void testPlayerGroup() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fourPM = Timestamp.valueOf("2011-10-21 16:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan fourToFive = new TimeSpan(fourPM, fivePM);
		
		SortedSet<TimeSpan> times1 = new ConcurrentSkipListSet<TimeSpan>();
		times1.add(threeToFive);
		times1.add(tenToThree);
		SortedSet<TimeSpan> times2 = new ConcurrentSkipListSet<TimeSpan>();
		times2.add(threeToFive);
		times2.add(fourToFive);
		
		Player p1 = new Player("P1", times1);
		Player p2 = new Player("P2", times2);
		PlayerGroup g1 = new PlayerGroup("G1", p1, p2);
		assertThat(g1.getPlayers().size(), equalTo(2));
		assertThat(g1.getPlayers().first(), equalTo(p2));
		assertThat(g1.getPlayers().first(), not(p1));
		assertThat(g1.getPlayers().last(), equalTo(p1));
		assertThat(g1.getAvailability().size(), equalTo(1));
		assertThat(g1.getAvailability().first().toString(), equalTo(threeToFive.toString()));
	}

}
