import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class RoundRobinTest {

	@Test
	public void testRoundRobin() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		Timestamp sixPM = Timestamp.valueOf("2011-10-21 18:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan fiveToSix = new TimeSpan(fivePM, sixPM);
		
		SortedSet<TimeSpan> times1 = new ConcurrentSkipListSet<TimeSpan>();
		times1.add(threeToFive);
		times1.add(tenToThree);
		SortedSet<TimeSpan> times2 = new ConcurrentSkipListSet<TimeSpan>();
		times2.add(tenToThree);
		times2.add(fiveToSix);
		
		Court c1 = new Court("C1", times1, "V1");
		Court c2 = new Court("C2", times2, "V1");
		CourtManager courts = new CourtManager(c1, c2);
		
		Player p1 = new Player("P1", times2);
		Player p2 = new Player("P2", times2);
		
		RoundRobin robin = new RoundRobin("Robbin' Round", 30, courts, p1, p2);
		Match firstMatch = robin.TryScheduleMatch();
		assertThat(firstMatch.toString(), equalTo("Robbin' Round match 1: P1 vs. P2 at C2 (V1), 10/21/11 5:30 PM - 6:00 PM"));
		assertThat(robin.FullyScheduled(), equalTo(true));
	}

}
