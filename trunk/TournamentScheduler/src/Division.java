import java.util.Vector;


public interface Division {
	
	public void LinkTo(Division next);
	
	public boolean TrySchedule(Vector<Match> schedule);
	
	public String Name();
	
	public int MinutesPerMatch();
	
}
