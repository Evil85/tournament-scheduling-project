import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Vector;

import org.junit.Test;


public class PoolPlayTest {

	@Test
	public void testPoolPlay() {
		Timestamp one = Timestamp.valueOf("2011-10-21 1:00:00.000000000");
		Timestamp two = Timestamp.valueOf("2011-10-21 2:00:00.000000000");
		Timestamp three = Timestamp.valueOf("2011-10-21 3:00:00.000000000");
		Timestamp four = Timestamp.valueOf("2011-10-21 4:00:00.000000000");
		Timestamp five = Timestamp.valueOf("2011-10-21 5:00:00.000000000");
		Timestamp six = Timestamp.valueOf("2011-10-21 6:00:00.000000000");
		Timestamp seven = Timestamp.valueOf("2011-10-21 7:00:00.000000000");
		Timestamp eight = Timestamp.valueOf("2011-10-21 8:00:00.000000000");
		Timestamp nine = Timestamp.valueOf("2011-10-21 9:00:00.000000000");
		Timestamp ten = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp eleven = Timestamp.valueOf("2011-10-21 11:00:00.000000000");
		TimeSpan oneS = new TimeSpan(one, two);
		TimeSpan twoS = new TimeSpan(two, three);
		TimeSpan threeS = new TimeSpan(three, four);
		TimeSpan fourS = new TimeSpan(four, five);
		TimeSpan fiveS = new TimeSpan(five, six);
		TimeSpan sixS = new TimeSpan(six, seven);
		TimeSpan sevenS = new TimeSpan(seven, eight);
		TimeSpan eightS = new TimeSpan(eight, nine);
		TimeSpan nineS = new TimeSpan(nine, ten);
		TimeSpan tenS = new TimeSpan(ten, eleven);
		
		Court c1 = new Court("C1", "V1", oneS, sixS, sevenS, eightS, nineS, tenS);
		Court c2 = new Court("C2", "V1", oneS, twoS, threeS, fourS, fiveS, sixS, nineS);
		CourtManager courts = new CourtManager(c1, c2);
		
		Player p1 = new Player("P1", oneS, twoS, sevenS, nineS, tenS);
		Player p2 = new Player("P2", oneS, sevenS, eightS, nineS, tenS);
		Player p3 = new Player("P3", twoS, sevenS, eightS, nineS, tenS);
		
		Player p4 = new Player("P4", oneS, twoS, sixS, sevenS, nineS, tenS);
		Player p5 = new Player("P5", oneS, fourS, fiveS, sevenS, eightS, nineS, tenS);
		Player p6 = new Player("P6", threeS, fiveS, sixS, sevenS, eightS, nineS, tenS);
		
		
		PoolPlay pool = new PoolPlay("X", 60, courts, p1, p2, p3, p4, p5, p6);
		pool.LinkTo(pool);
		Vector<Match> schedule = new Vector<Match>();
		assertThat(pool.TrySchedule(schedule), equalTo(true));
		assertThat(schedule.size(), equalTo(9));
		System.out.println(schedule);
		assertThat(schedule.elementAt(0).toString(), equalTo("X final match: Finalist A vs. Finalist B at C1 (V1), 10/21/11 10:00 AM - 11:00 AM"));
		assertThat(schedule.elementAt(1).toString(), equalTo("X semifinal match 1: Winner A vs. Runner-up A at C1 (V1), 10/21/11 9:00 AM - 10:00 AM"));
		assertThat(schedule.elementAt(2).toString(), equalTo("X semifinal match 2: Winner B vs. Runner-up B at C1 (V1), 10/21/11 7:00 AM - 8:00 AM"));
		assertThat(schedule.elementAt(3).toString(), equalTo("X round A match 1: P1 vs. P2 at C1 (V1), 10/21/11 1:00 AM - 2:00 AM"));
		assertThat(schedule.elementAt(4).toString(), equalTo("X round A match 2: P1 vs. P3 at C2 (V1), 10/21/11 2:00 AM - 3:00 AM"));
		assertThat(schedule.elementAt(5).toString(), equalTo("X round A match 3: P2 vs. P3 at C1 (V1), 10/21/11 8:00 AM - 9:00 AM"));
		assertThat(schedule.elementAt(6).toString(), equalTo("X round B match 1: P4 vs. P5 at C2 (V1), 10/21/11 1:00 AM - 2:00 AM"));
		assertThat(schedule.elementAt(7).toString(), equalTo("X round B match 2: P4 vs. P6 at C1 (V1), 10/21/11 6:00 AM - 7:00 AM"));
		assertThat(schedule.elementAt(8).toString(), equalTo("X round B match 3: P5 vs. P6 at C2 (V1), 10/21/11 5:00 AM - 6:00 AM"));
	}
}