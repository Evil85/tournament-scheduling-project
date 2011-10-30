import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
		
		Match m = new Match("Title Match", t1, t2);
		assertThat(m.toString(), equalTo("Title Match: T1 vs. T2"));
		assertThat(m.getTime(), equalTo(null));
		m.Schedule(c1, tenToThree);
		assertThat(m.toString(), equalTo("Title Match: T1 vs. T2 at C1 (V1), 10/21/11 10:00 AM - 3:00 PM"));
		assertThat(m.getTime(), equalTo(tenToThree));
	}
	
	@Test
	public void testAvailableTimes() {
		Timestamp sixAM = Timestamp.valueOf("2011-10-21 6:00:00.000000000");
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp elevenAM = Timestamp.valueOf("2011-10-21 11:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fourPM = Timestamp.valueOf("2011-10-21 16:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan sixToEleven = new TimeSpan(sixAM, elevenAM);
		TimeSpan elevenToThree = new TimeSpan(elevenAM, threePM);
		TimeSpan tenToFour = new TimeSpan(tenAM, fourPM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan threeToFour = new TimeSpan(threePM, fourPM);
		
		SortedSet<TimeSpan> allTimes = new ConcurrentSkipListSet<TimeSpan>();
		allTimes.add(sixToEleven);
		allTimes.add(elevenToThree);
		allTimes.add(threeToFive);
		SortedSet<TimeSpan> firstTime = new ConcurrentSkipListSet<TimeSpan>();
		firstTime.add(sixToEleven);
		SortedSet<TimeSpan> secondTime = new ConcurrentSkipListSet<TimeSpan>();
		secondTime.add(tenToFour);
		SortedSet<TimeSpan> thirdTime = new ConcurrentSkipListSet<TimeSpan>();
		thirdTime.add(threeToFive);
		
		Court c1 = new Court("C1", allTimes, "V1");
		Player p1a = new Player("P1a", firstTime);
		Player p1b = new Player("P1b", firstTime);
		Player p2a = new Player("P2a", secondTime);
		Player p2b = new Player("P2b", secondTime);
		Player p3a = new Player("P3a", thirdTime);
		Player p3b = new Player("P3b", thirdTime);
		Player p4a = new Player("P4a", allTimes);
		Player p4b = new Player("P4b", allTimes);
		PlayerGroup g1a = new PlayerGroup(p1a);
		PlayerGroup g1b = new PlayerGroup(p1b);
		PlayerGroup g2a = new PlayerGroup(p2a);
		PlayerGroup g2b = new PlayerGroup(p2b);
		PlayerGroup g3a = new PlayerGroup(p3a);
		PlayerGroup g3b = new PlayerGroup(p3b);
		PlayerGroup g4a = new PlayerGroup(p4a);
		PlayerGroup g4b = new PlayerGroup(p4b);
		
		Match m1 = new Match("M1", g1a, g1b);
		Match m2 = new Match("M2", g2a, g2b);
		Match m3 = new Match("M3", g3a, g3b);
		Match m4 = new Match("M4", g4a, g4b);
		
		assertThat(m1.getAvailability(c1, m2, m3, m4).toString(), equalTo("[10/21/11 6:00 AM - 11:00 AM]"));
		assertThat(m2.getAvailability(c1, m1, m3, m4).toString(), equalTo("[10/21/11 10:00 AM - 4:00 PM]"));
		assertThat(m3.getAvailability(c1, m1, m2, m4).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(m4.getAvailability(c1, m1, m2, m3).toString(), equalTo("[10/21/11 6:00 AM - 5:00 PM]"));
		
		m1.Schedule(c1, sixToEleven);
		assertThat(m1.getAvailability(c1, m2, m3, m4).toString(), equalTo("[10/21/11 6:00 AM - 11:00 AM]"));
		assertThat(m2.getAvailability(c1, m1, m3, m4).toString(), equalTo("[10/21/11 11:00 AM - 4:00 PM]"));
		assertThat(m3.getAvailability(c1, m1, m2, m4).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(m4.getAvailability(c1, m1, m2, m3).toString(), equalTo("[10/21/11 11:00 AM - 5:00 PM]"));
		
		m3.Schedule(c1, threeToFour);
		assertThat(m1.getAvailability(c1, m2, m3, m4).toString(), equalTo("[10/21/11 6:00 AM - 11:00 AM]"));
		assertThat(m2.getAvailability(c1, m1, m3, m4).toString(), equalTo("[10/21/11 11:00 AM - 3:00 PM]"));
		assertThat(m3.getAvailability(c1, m1, m2, m4).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(m4.getAvailability(c1, m1, m2, m3).toString(), equalTo("[10/21/11 11:00 AM - 3:00 PM, 10/21/11 4:00 PM - 5:00 PM]"));
		
		m4.Schedule(c1, elevenToThree);
		assertThat(m1.getAvailability(c1, m2, m3, m4).toString(), equalTo("[10/21/11 6:00 AM - 11:00 AM]"));
		assertThat(m2.getAvailability(c1, m1, m3, m4).toString(), equalTo("[]"));
		assertThat(m3.getAvailability(c1, m1, m2, m4).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(m4.getAvailability(c1, m1, m2, m3).toString(), equalTo("[10/21/11 11:00 AM - 3:00 PM, 10/21/11 4:00 PM - 5:00 PM]"));
	}

}
