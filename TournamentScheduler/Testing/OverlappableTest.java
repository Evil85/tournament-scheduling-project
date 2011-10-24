import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class OverlappableTest {

	@Test
	public void testOverlappable() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		
		SortedSet<TimeSpan> times = new ConcurrentSkipListSet<TimeSpan>();
		times.add(threeToFive);
		times.add(tenToThree);
		
		Overlappable testOverlap = new Overlappable("Test Overlap", times);
		assertEquals(testOverlap.m_name, "Test Overlap");
		assertEquals(testOverlap.m_availability, times);
		assertEquals(testOverlap.m_nAvailableMinutes, 420);
	}

	@Test
	public void testToString() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		
		SortedSet<TimeSpan> times = new ConcurrentSkipListSet<TimeSpan>();
		times.add(threeToFive);
		times.add(tenToThree);
		
		Overlappable testOverlap = new Overlappable("Test Overlap", times);
		assertEquals(testOverlap.toString(), "Test Overlap : [10/21/11 10:00 AM - 3:00 PM, 10/21/11 3:00 PM - 5:00 PM]");
	}

}
