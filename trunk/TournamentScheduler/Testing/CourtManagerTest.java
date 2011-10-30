import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class CourtManagerTest {

	@Test
	public void testCourtManager() {
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
		
		PlayerGroup t1 = new PlayerGroup("T1", new Player("P1", times2));
		PlayerGroup t2 = new PlayerGroup("T2", new Player("P2", times2));
		Match m = new Match("Title Match", t1, t2);
		
		assertThat(courts.TryScheduleLatest(m, 20), equalTo(true));
		assertThat(m.getCourt(), equalTo(c2));
		TimeSpan expected = new TimeSpan(Timestamp.valueOf("2011-10-21 17:40:00.000000000"), sixPM);
		assertThat(m.getTime().toString(), equalTo(expected.toString()));
	}

}
