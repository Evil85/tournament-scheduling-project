import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;


public class OverlappableTest {

	@Test
	public void testOverlappable() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fourPM = Timestamp.valueOf("2011-10-21 16:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan fourToFive = new TimeSpan(fourPM, fivePM);
		
		SortedSet<TimeSpan> times = new ConcurrentSkipListSet<TimeSpan>();
		times.add(threeToFive);
		times.add(tenToThree);
		SortedSet<TimeSpan> timesBefore = new ConcurrentSkipListSet<TimeSpan>(times);
		
		Overlappable testOverlap = new Overlappable("Test Overlap", times);
		assertThat(testOverlap.getName(), equalTo("Test Overlap"));
		assertThat(testOverlap.getAvailability(), equalTo(times));
		assertThat(testOverlap.getAvailability().size(), equalTo(1));
		assertThat(testOverlap.getAvailability().first().getStart(), equalTo(tenAM));
		assertThat(testOverlap.getAvailability().first().getEnd(), equalTo(fivePM));
		assertThat(timesBefore.toString(), not(times.toString()));
		assertThat(testOverlap.getAvailableMinutes(), equalTo(420));
		
		Overlappable duplicate = new Overlappable("Duplicate", times);
		Overlappable copy = new Overlappable("Copy", timesBefore);
		assertThat(testOverlap, not(duplicate));
		assertThat(testOverlap, not(copy));
		copy.getAvailability().clear();
		assertThat(testOverlap.getAvailability().isEmpty(), not(true));
		duplicate.getAvailability().clear();
		assertThat(testOverlap.getAvailability().isEmpty(), equalTo(true));
		assertThat(testOverlap, not(duplicate));
		
		testOverlap.getAvailability().add(tenToThree);
		SortedSet<TimeSpan> times2 = new ConcurrentSkipListSet<TimeSpan>();
		times2.add(fourToFive);
		Overlappable testOverlap2 = new Overlappable("Test Overlap 2", times2);
		assertThat(testOverlap, not(testOverlap2));
		Overlappable testOverlap3 = new Overlappable("Combined Overlap", testOverlap, testOverlap2);
		Overlappable testOverlap4 = new Overlappable("Combined Overlap", testOverlap3);
		assertThat(testOverlap.getAvailability().size(), equalTo(1));
		assertThat(testOverlap2.getAvailability().size(), equalTo(1));
		assertThat(testOverlap3.getAvailability().size(), equalTo(0));
		assertThat(testOverlap3, equalTo(testOverlap4));
		testOverlap4.getAvailability().add(fourToFive);
		assertThat(testOverlap3, not(testOverlap4));
	}

	@Test
	public void testToString() {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fourPM = Timestamp.valueOf("2011-10-21 16:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		TimeSpan fourToFive = new TimeSpan(fourPM, fivePM);
		
		SortedSet<TimeSpan> times1 = new ConcurrentSkipListSet<TimeSpan>();
		times1.add(threeToFive);
		times1.add(tenToThree);
		SortedSet<TimeSpan> times2 = new ConcurrentSkipListSet<TimeSpan>();
		times2.add(tenToThree);
		times2.add(fourToFive);
		
		Overlappable testOverlap1 = new Overlappable("Test Overlap 1", times1);
		Overlappable testOverlap2 = new Overlappable("Test Overlap 2", times2);
		assertThat(testOverlap1.toString(), equalTo("Test Overlap 1 : [10/21/11 10:00 AM - 5:00 PM]"));
		assertThat(testOverlap2.toString(), equalTo("Test Overlap 2 : [10/21/11 10:00 AM - 3:00 PM, 10/21/11 4:00 PM - 5:00 PM]"));
	}

}
