import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;


public class TimeSpanTest {
	
	@Test
	public void testTimeSpan() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threeOhOne = Timestamp.valueOf("2011-10-21 15:01:00.000000000");
		TimeSpan span = new TimeSpan(tenAM, threeOhOne);
		assertEquals(span.m_start, tenAM);
		assertEquals(span.m_end, threeOhOne);
		assertEquals(span.m_nMinutes, 301);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpanIllegalOrder() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		new TimeSpan(threePM, tenAM);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpanIllegalPrecisionS() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:01.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		new TimeSpan(threePM, tenAM);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpanIllegalPrecisionMs() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.001000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		new TimeSpan(threePM, tenAM);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpanIllegalPrecisionNs() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000001");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		new TimeSpan(threePM, tenAM);
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTimeSpanIllegalZeroSpan() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp tenAMAgain = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		new TimeSpan(tenAM, tenAMAgain);
		fail();
	}

	@Test
	public void testCompareTo() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan before = new TimeSpan(tenAM, threePM);
		TimeSpan after = new TimeSpan(threePM, fivePM);
		TimeSpan whole = new TimeSpan(tenAM, fivePM);
		assertTrue(before.compareTo(after) < 0);
		assertTrue(after.compareTo(before) > 0);
		assertTrue(whole.compareTo(before) == 0);
		assertTrue(whole.compareTo(after) == 0);
	}

	@Test
	public void testToString() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		TimeSpan span = new TimeSpan(tenAM, threePM);
		assertEquals(span.toString(), "10/21/11 10:00 AM - 3:00 PM");
	}

	@Test
	public void testOverlaps() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp oneToNoon = Timestamp.valueOf("2011-10-21 11:59:00.000000000");
		Timestamp noon = Timestamp.valueOf("2011-10-21 12:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan noonToFive = new TimeSpan(noon, fivePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		assertTrue(tenToThree.Overlaps(tenToThree));
		assertTrue(tenToThree.Overlaps(noonToFive));
		assertTrue(noonToFive.Overlaps(tenToThree));
		assertTrue(!tenToThree.Overlaps(threeToFive));
		assertTrue(!threeToFive.Overlaps(tenToThree));
		assertTrue(noonToFive.Overlaps(threeToFive));
		TimeSpan oneToNoonToThree = new TimeSpan(oneToNoon, threePM);
		TimeSpan tenToNoon = new TimeSpan(tenAM, noon);
		assertTrue(oneToNoonToThree.Overlaps(tenToNoon));
		assertTrue(tenToNoon.Overlaps(oneToNoonToThree));
	}

}
