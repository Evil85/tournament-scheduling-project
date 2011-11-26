import java.util.Comparator;


public class MostRestrictiveTeam implements Comparator<Team> {

	private static MostRestrictiveTeam instance = null;
	
	protected MostRestrictiveTeam() {}
	
	public static MostRestrictiveTeam getInstance()
	{
		if (instance == null)
			instance = new MostRestrictiveTeam();
		
		return instance;
	}

	public int compare(Team first, Team second)
	{
		return first.AvailableMinutes() - second.AvailableMinutes();
	}
	
}
