import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Vector;

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
		
		Court c1 = new Court("C1", "V1", threeToFive, tenToThree);
		Court c2 = new Court("C2", "V1", tenToThree, fiveToSix);
		CourtManager courts = new CourtManager(c1, c2);
		
		Player p1 = new Player("P1", tenToThree, fiveToSix);
		Player p2 = new Player("P2", tenToThree, fiveToSix);
		
		RoundRobin robin = new RoundRobin("Robbin' Round", 30, courts, p1, p2);
		Vector<Match> schedule = new Vector<Match>();
		assertThat(robin.TrySchedule(schedule), equalTo(true));
		assertThat(schedule.firstElement().toString(), equalTo("Robbin' Round match 1: P1 vs. P2 at C2 (V1), 10/21/11 5:30 PM - 6:00 PM"));
	}

}
