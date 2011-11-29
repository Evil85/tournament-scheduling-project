import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;
import java.util.Vector;

import org.junit.Test;

import com.AvailabilityType;
import com.Court;
import com.CourtManager;
import com.CourtTime;
import com.Division;
import com.Match;
import com.Player;
import com.RoundRobin;
import com.SchedulingUtil;
import com.Team;
import com.TimeSpan;

public class MatchTest {

	@Test
	public void testToString() {
		Division dummy = new RoundRobin(-1, "dud", -1, new CourtManager(), new Player(1, "nobody"));

		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);

		Team t1 = new Team("T1", new Player(1, "P1", tenToThree));
		Team t2 = new Team("T2", new Player(2, "P2", tenToThree));
		Court c1 = new Court(1, "C1", "V1", tenToThree);

		Match m = new Match("Title Match", dummy, t1, t2);
		assertThat(m.toString(), equalTo("Title Match: T1 vs. T2"));
		assertThat(m.Time(), equalTo(null));
		m.Schedule(new CourtTime(c1, tenToThree));
		assertThat(m.toString(), equalTo("Title Match: T1 vs. T2 at C1 (V1), 10/21/11 10:00 AM - 3:00 PM"));
		assertThat(m.Time(), equalTo(tenToThree));
	}

	@Test
	public void testAvailableTimes() {
		Division dummy = new RoundRobin(-1, "dud", -1, new CourtManager(), new Player(1, "nobody"));

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

		Court c1 = new Court(1, "C1", "V1", sixToEleven, elevenToThree, threeToFive);
		Player p1a = new Player(1, "P1a", sixToEleven);
		Player p1b = new Player(2, "P1b", sixToEleven);
		Player p2a = new Player(3, "P2a", tenToFour);
		Player p2b = new Player(4, "P2b", tenToFour);
		Player p3a = new Player(5, "P3a", threeToFive);
		Player p3b = new Player(6, "P3b", threeToFive);
		Player p4a = new Player(7, "P4a", sixToEleven, elevenToThree, threeToFive);
		Player p4b = new Player(8, "P4b", sixToEleven, elevenToThree, threeToFive);
		Team g1a = new Team(p1a.Name(), p1a);
		Team g1b = new Team(p1b.Name(), p1b);
		Team g2a = new Team(p2a.Name(), p2a);
		Team g2b = new Team(p2b.Name(), p2b);
		Team g3a = new Team(p3a.Name(), p3a);
		Team g3b = new Team(p3b.Name(), p3b);
		Team g4a = new Team(p4a.Name(), p4a);
		Team g4b = new Team(p4b.Name(), p4b);

		Match m1 = new Match("M1", dummy, g1a, g1b);
		Match m2 = new Match("M2", dummy, g2a, g2b);
		Match m3 = new Match("M3", dummy, g3a, g3b);
		Match m4 = new Match("M4", dummy, g4a, g4b);
		Vector<Match> schedule = new Vector<Match>();

		assertThat(SchedulingUtil.MatchAvailability(m1, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 6:00 AM - 11:00 AM]"));
		assertThat(SchedulingUtil.MatchAvailability(m2, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 10:00 AM - 4:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m3, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m4, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 6:00 AM - 5:00 PM]"));

		m1.Schedule(new CourtTime(c1, sixToEleven));
		schedule.add(m1);
		assertThat(SchedulingUtil.MatchAvailability(m1, c1, schedule, AvailabilityType.All).toString(), equalTo("[]"));
		assertThat(SchedulingUtil.MatchAvailability(m2, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 11:00 AM - 4:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m3, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 3:00 PM - 5:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m4, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 11:00 AM - 5:00 PM]"));

		m3.Schedule(new CourtTime(c1, threeToFour));
		schedule.add(m3);
		assertThat(SchedulingUtil.MatchAvailability(m1, c1, schedule, AvailabilityType.All).toString(), equalTo("[]"));
		assertThat(SchedulingUtil.MatchAvailability(m2, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 11:00 AM - 3:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m3, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 4:00 PM - 5:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m4, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 11:00 AM - 3:00 PM, 10/21/11 4:00 PM - 5:00 PM]"));

		m4.Schedule(new CourtTime(c1, elevenToThree));
		schedule.add(m4);
		assertThat(SchedulingUtil.MatchAvailability(m1, c1, schedule, AvailabilityType.All).toString(), equalTo("[]"));
		assertThat(SchedulingUtil.MatchAvailability(m2, c1, schedule, AvailabilityType.All).toString(), equalTo("[]"));
		assertThat(SchedulingUtil.MatchAvailability(m3, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 4:00 PM - 5:00 PM]"));
		assertThat(SchedulingUtil.MatchAvailability(m4, c1, schedule, AvailabilityType.All).toString(), equalTo("[10/21/11 4:00 PM - 5:00 PM]"));
	}

}
