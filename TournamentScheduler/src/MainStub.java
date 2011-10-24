import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class MainStub {
	public static void main(String[] args) {
		Timestamp tenAM = Timestamp.valueOf("2011-10-21 10:00:00.000000000");
		Timestamp threePM = Timestamp.valueOf("2011-10-21 15:00:00.000000000");
		Timestamp fivePM = Timestamp.valueOf("2011-10-21 17:00:00.000000000");
		TimeSpan tenToThree = new TimeSpan(tenAM, threePM);
		System.out.println(tenToThree);
		TimeSpan threeToFive = new TimeSpan(threePM, fivePM);
		System.out.println(threeToFive);
		if (tenToThree.Overlaps(threeToFive))
			System.out.println("They overlap.");
		else
			System.out.println("They don't overlap.");
		
		SortedSet<TimeSpan> times = new ConcurrentSkipListSet<TimeSpan>();
		times.add(threeToFive);
		times.add(tenToThree);
		System.out.println(times);
		
		Overlappable testOverlap = new Overlappable("Test Overlap", times);
		System.out.println(testOverlap);
	}

}
