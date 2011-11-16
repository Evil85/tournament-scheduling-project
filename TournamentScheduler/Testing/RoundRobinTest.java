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
		Court c2 = new Court("C2", "V2", tenToThree, fiveToSix);
		CourtManager courts = new CourtManager(c1, c2);
		
		Player p1 = new Player("P1", tenToThree, fiveToSix);
		Player p2 = new Player("P2", tenToThree, fiveToSix);
		Player p3 = new Player("P3", tenToThree);
		
		RoundRobin robin = new RoundRobin("Robbin' Round", 30, courts, p1, p2, p3);
		robin.LinkTo(robin);
		Vector<Match> schedule = new Vector<Match>();
		assertThat(robin.TrySchedule(schedule), equalTo(true));
		String strSchedule = "";
		for (Match m : schedule)
			strSchedule += m.toString() + "; ";
		assertThat(strSchedule, equalTo("Robbin' Round match 1: P1 vs. P2 at C2 (V2), 10/21/11 5:30 PM - 6:00 PM; " +
				"Robbin' Round match 2: P1 vs. P3 at C1 (V1), 10/21/11 2:30 PM - 3:00 PM; " +
				"Robbin' Round match 3: P2 vs. P3 at C1 (V1), 10/21/11 1:30 PM - 2:00 PM; "));
		assertThat(p1.Availability().toString(), equalTo("[10/21/11 10:00 AM - 2:30 PM, 10/21/11 5:00 PM - 5:30 PM]"));
		
		// Try again with longer matches to force non-ideal matches.
		c1 = new Court("C1", "V1", threeToFive, tenToThree);
		c2 = new Court("C2", "V2", tenToThree, fiveToSix);
		courts = new CourtManager(c1, c2);
		
		p1 = new Player("P1", tenToThree, fiveToSix);
		p2 = new Player("P2", tenToThree, fiveToSix);
		p3 = new Player("P3", tenToThree);
		robin = new RoundRobin("SLG Tournament", 70, courts, p1, p2, p3);
		robin.LinkTo(robin);
		schedule = new Vector<Match>();
		assertThat(robin.TrySchedule(schedule), equalTo(true));
		strSchedule = "";
		for (Match m : schedule)
			strSchedule += m.toString() + "; ";
		assertThat(strSchedule, equalTo("SLG Tournament match 1: P1 vs. P2 at C1 (V1), 10/21/11 1:50 PM - 3:00 PM; " +
				"SLG Tournament match 2: P1 vs. P3 at C1 (V1), 10/21/11 11:30 AM - 12:40 PM; " +
				"SLG Tournament match 3: P2 vs. P3 at C1 (V1), 10/21/11 10:20 AM - 11:30 AM; "));
	}

}