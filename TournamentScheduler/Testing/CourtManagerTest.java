import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Vector;

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
		
		Court c1 = new Court("C1", "V1", threeToFive, tenToThree);
		Court c2 = new Court("C2", "V1", tenToThree, fiveToSix);
		CourtManager courts = new CourtManager(c1, c2);
		
		Team t1 = new Team("T1", new Player("P1", tenToThree, fiveToSix));
		Team t2 = new Team("T2", new Player("P2", tenToThree, fiveToSix));
		Match m = new Match("Title Match", t1, t2);
		Vector<Match> schedule = new Vector<Match>();
		
		assertThat(courts.TryScheduleLatest(m, 20, schedule), equalTo(true));
		assertThat(m.Court(), equalTo(c2));
		TimeSpan expected = new TimeSpan(Timestamp.valueOf("2011-10-21 17:40:00.000000000"), sixPM);
		assertThat(m.Time().toString(), equalTo(expected.toString()));
	}

}
