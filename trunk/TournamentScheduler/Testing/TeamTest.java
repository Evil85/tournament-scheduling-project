import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;

import org.junit.Test;


public class TeamTest {

	@Test
	public void testTeam() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fourPM = Timestamp.valueOf("2011-10-21 16:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan fourToFive = new TimeSpan(fourPM, fivePM);
		
		Player p1 = new Player("P1", threeToFive, tenToThree);
		Player p2 = new Player("P2", threeToFive, fourToFive);
		Team g1 = new Team("G1", p1, p2);
		assertThat(g1.Players().size(), equalTo(2));
		assertThat(g1.Players().contains(p1), equalTo(true));
		assertThat(g1.Players().contains(p2), equalTo(true));
		assertThat(g1.Availability().size(), equalTo(1));
		assertThat(g1.Availability().first().toString(), equalTo(threeToFive.toString()));
	}

}
