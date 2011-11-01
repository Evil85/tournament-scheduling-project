import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;

import org.junit.Test;


public class CourtTest {

	@Test
	public void testCourt() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		
		Court testCourt = new Court("Test Court", "Test Venue", threeToFive, tenToThree);
		assertThat(testCourt.toString(), equalTo("Test Court (Test Venue) : [10/21/11 10:00 AM - 5:00 PM]"));
		assertThat(testCourt.Venue(), equalTo("Test Venue"));
	}

}
