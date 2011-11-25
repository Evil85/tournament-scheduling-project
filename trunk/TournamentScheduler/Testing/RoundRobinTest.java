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
		
		Court c1 = new Court(1, "C1", "V1", threeToFive, tenToThree);
		Court c2 = new Court(2, "C2", "V2", tenToThree, fiveToSix);
		CourtManager courts = new CourtManager(c1, c2);
		
		Player p1 = new Player(1, "P1", tenToThree, fiveToSix);
		Player p2 = new Player(2, "P2", tenToThree, fiveToSix);
		Player p3 = new Player(3, "P3", tenToThree);
		
		RoundRobin robin = new RoundRobin(1, "Robbin' Round", 30, courts, p1, p2, p3);
		robin.LinkTo(robin);
		Vector<Match> schedule = new Vector<Match>();
		assertThat(robin.TrySchedule(schedule), equalTo(true));
		assertThat(schedule.size(), equalTo(3));
		assertThat(schedule.elementAt(0).toString(), equalTo("Robbin' Round match 1: P1 vs. P2 at C2 (V2), 10/21/11 5:30 PM - 6:00 PM"));
		assertThat(schedule.elementAt(1).toString(), equalTo("Robbin' Round match 2: P1 vs. P3 at C1 (V1), 10/21/11 2:30 PM - 3:00 PM"));
		assertThat(schedule.elementAt(2).toString(), equalTo("Robbin' Round match 3: P2 vs. P3 at C1 (V1), 10/21/11 1:30 PM - 2:00 PM"));
		
		// Try again with longer matches to force uncomfortably close matches.
		c1 = new Court(1, "C1", "V1", threeToFive, tenToThree);
		c2 = new Court(2, "C2", "V2", tenToThree, fiveToSix);
		courts = new CourtManager(c1, c2);
		
		p1 = new Player(4, "P1", tenToThree, fiveToSix);
		p2 = new Player(5, "P2", tenToThree, fiveToSix);
		p3 = new Player(6, "P3", tenToThree);
		robin = new RoundRobin(2, "SLG Tournament", 80, courts, p1, p2, p3);
		robin.LinkTo(robin);
		schedule = new Vector<Match>();
		assertThat(robin.TrySchedule(schedule), equalTo(true));
		assertThat(schedule.size(), equalTo(3));
		assertThat(schedule.elementAt(0).toString(), equalTo("SLG Tournament match 1: P1 vs. P2 at C1 (V1), 10/21/11 12:40 PM - 2:00 PM"));
		assertThat(schedule.elementAt(1).toString(), equalTo("SLG Tournament match 2: P1 vs. P3 at C1 (V1), 10/21/11 10:00 AM - 11:20 AM"));
		assertThat(schedule.elementAt(2).toString(), equalTo("SLG Tournament match 3: P2 vs. P3 at C1 (V1), 10/21/11 11:20 AM - 12:40 PM"));
	}
}