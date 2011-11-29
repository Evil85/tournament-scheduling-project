import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.Court;
import com.CourtManager;
import com.CourtTime;
import com.Division;
import com.Match;
import com.Player;
import com.RoundRobin;
import com.Team;
import com.TimeSpan;

public class CourtManagerTest {

	@Test
	public void testCourtManager() {
		Division dummy = new RoundRobin(-1, "dud", -1, new CourtManager(), new Player(1, "nobody"));

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

		Team t1 = new Team("T1", new Player(1, "P1", tenToThree, fiveToSix));
		Team t2 = new Team("T2", new Player(2, "P2", tenToThree, fiveToSix));
		Match m = new Match("Title Match", dummy, t1, t2);
		Vector<Match> schedule = new Vector<Match>();

		List<CourtTime> times = courts.CourtTimesByLatest(m, 20, schedule);
		assertThat(times.size(), equalTo(33));

		m.Schedule(times.get(0));
		schedule.add(m);
		assertThat(m.Court(), equalTo(c2));
		TimeSpan expected = new TimeSpan(Timestamp.valueOf("2011-10-21 17:40:00.000000000"), sixPM);
		assertThat(m.Time().toString(), equalTo(expected.toString()));

		Match o = new Match("Other Match", dummy, t1, t2);
		times = courts.CourtTimesByLatest(o, 20, schedule);
		assertThat(times.size(), equalTo(32));
	}

}
