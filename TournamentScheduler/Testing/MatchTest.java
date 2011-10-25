import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class MatchTest {

	@Test
	public void testToString() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		
		SortedSet<TimeSpan> times = new ConcurrentSkipListSet<TimeSpan>();
		times.add(tenToThree);
		
		PlayerGroup t1 = new PlayerGroup("T1", new Player("P1", times));
		PlayerGroup t2 = new PlayerGroup("T2", new Player("P2", times));
		Court c1 = new Court("C1", times, "V1");
		
		Match m = new Match(c1, t1, t2, tenToThree);
		assertEquals(m.toString(), "T1 vs. T2 at C1 (V1), 10/21/11 10:00 AM - 3:00 PM");
	}

}
