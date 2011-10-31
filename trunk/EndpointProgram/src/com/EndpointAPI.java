package com;

import org.apache.log4j.Logger;
import com.Utilities.CommandArguments;
import java.sql.*;

/**
 * User: Chris
 * <p/>
 * Date: 10/5/11
 * <p/>
 * EndpointAPI is used to
 */
public class EndpointAPI
{
	private static final Logger logger = Logger.getLogger(EndpointAPI.class);

	public void createUser(CommandArguments arguments)
	{
        try {
            String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
            String user = "admtourn201140";
            String pass = "yinvamOph";
	        Connection conn = DriverManager.getConnection(URL, user, pass);
	        Statement st = conn.createStatement();
	
	        ResultSet rs = st.executeQuery("SELECT `pid` FROM `person` WHERE `name` = '" + arguments.getArgument("PersonName") + "'");
	        rs.next();
	        int pid = Integer.parseInt(rs.getString(1));

	        st.executeUpdate ("INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `pid_person`) VALUES ('"
	        + arguments.getArgument("UserName") + "', '" 
	        + arguments.getArgument("Password") + "', " 
	        + "CAST(CURRENT_TIMESTAMP AS DATE), '" 
	        + arguments.getArgument("Permissions") + "', " 
	        + pid +");");

	        st.close();
        }
        catch (SQLException ex)
        {
	        System.out.println("SQL exception");
	        ex.printStackTrace();
	        return;
        }
        catch (Exception ex)
        {
	        System.out.println("Other exception");
	        ex.printStackTrace();
	        return;
        }
	
		logger.info("User Created: " + arguments.getArgument("Username"));
	}
	
	public void createPerson(CommandArguments arguments)
	{
	    try {
		    String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
	        String user = "admtourn201140";
	        String pass = "yinvamOph";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			Statement st = conn.createStatement();
			
			st.executeUpdate ("INSERT INTO `person` (`name`, `email`, `address`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `lid_homeClub`) VALUES ('"
			+ arguments.getArgument("PersonName") + "', '" 
			+ arguments.getArgument("Email") + "', '" 
			+ arguments.getArgument("Address") + "', '" 
			+ arguments.getArgument("Phone") + "', '" 
			+ arguments.getArgument("Gender") + "', CAST('" 
			+ arguments.getArgument("Birthdate") + "' AS DATE), " 
			+ "null, null, null, null, null);");

			st.close();
		}
		catch (SQLException ex)
		{
			System.out.println("SQL exception");
			ex.printStackTrace();
			return;
		}
		catch (Exception ex)
		{
			System.out.println("Other exception");
			ex.printStackTrace();
			return;
		}
	
		logger.info("Person Created: " + arguments.getArgument("Name"));
	}
	
	public void createLocation(CommandArguments arguments)
	{
	    try {
            String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
            String user = "admtourn201140";
            String pass = "yinvamOph";
	        Connection conn = DriverManager.getConnection(URL, user, pass);
	        Statement st = conn.createStatement();
	
	        st.executeUpdate ("INSERT INTO `location` (`name`, `address`, `city`, `state`, `zip`, `phone`, `weekdayOpenTime`, `weekdayCloseTime`, `weekendOpenTime`, `weekendCloseTime`) VALUES ('"
	        + arguments.getArgument("LocName") + "', '" 
	        + arguments.getArgument("Address") + "', '" 
	        + arguments.getArgument("City") + "', '" 
            + arguments.getArgument("State") + "', '" 
	        + arguments.getArgument("Zip") + "', '" 
	        + arguments.getArgument("Phone") + "', " 
	        + "CAST('" + arguments.getArgument("weekdayOpenTime") +"' AS TIME)" + ", " 
	        + "CAST('" + arguments.getArgument("weekdayCloseTime") +"' AS TIME)" + ", " 
	        + "CAST('" + arguments.getArgument("weekendOpenTime") +"' AS TIME)" + ", " 
	        + "CAST('" + arguments.getArgument("weekendCloseTime") +"' AS TIME));");

	        st.close();
        }
        catch (SQLException ex)
        {
	        System.out.println("SQL exception");
	        ex.printStackTrace();
	        return;
        }
        catch (Exception ex)
        {
	        System.out.println("Other exception");
	        ex.printStackTrace();
	        return;
        }
	
		logger.info("Location Created: " + arguments.getArgument("Name"));
	}
	
	public void createCourt(CommandArguments arguments)
	{
	    try {
            String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
            String user = "admtourn201140";
            String pass = "yinvamOph";
	        Connection conn = DriverManager.getConnection(URL, user, pass);
	        Statement st = conn.createStatement();
	
	        ResultSet rs = st.executeQuery("SELECT `lid` FROM `location` WHERE `name` = '" + arguments.getArgument("LocationName") + "'");
	        rs.next();
	        int lid = Integer.parseInt(rs.getString(1));

	        st.executeUpdate ("INSERT INTO `court` (`courtName`, `lid_location`) VALUES('"
	        + arguments.getArgument("CourtName") + "', "
	        + lid +");");

	        st.close();
        }
        catch (SQLException ex)
        {
	        System.out.println("SQL exception");
	        ex.printStackTrace();
	        return;
        }
        catch (Exception ex)
        {
	        System.out.println("Other exception");
	        ex.printStackTrace();
	        return;
        }
        
		logger.info("Court Created: " + arguments.getArgument("Name"));
	}
	
	public void createTournament(CommandArguments arguments)
	{
	        String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
            String user = "admtourn201140";
            String pass = "yinvamOph";
	        Connection conn = DriverManager.getConnection(URL, user, pass);
	        Statement st = conn.createStatement();
	
	        ResultSet rs = st.executeQuery("SELECT `pid` FROM `person` WHERE `name` = '" + arguments.getArgument("OwnerName") + "'");
	        rs.next();
	        int pid = Integer.parseInt(rs.getString(1));

	        st.executeUpdate ("INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `uid_owner`)
VALUES (`"
	        + arguments.getArgument("TournamentName") + "', " 
	        + "CAST('" + arguments.getArgument("StartDate") + "' AS DATE), " 
   	        + "CAST('" + arguments.getArgument("EndDate") + "' AS DATE), "
	        + arguments.getArgument("GuestViewable") + ", "
   	        + arguments.getArgument("TravelTime") + ", "
	        + "CAST('" + arguments.getArgument("StartTimeWeekdays") + "' AS TIME), "
	        + "CAST('" + arguments.getArgument("EndTimeWeekdays") + "' AS TIME), "
	        + "CAST('" + arguments.getArgument("StartTimeWeekends") + "' AS TIME), "
	        + "CAST('" + arguments.getArgument("EndTimeWeekends") + "' AS TIME), "
	        + arguments.getArgument("MaxDivisions") + ", "
	        + pid +");");

	        st.close();
	
		logger.info("Tournament Created: " + arguments.getArgument("Name"));
	}
	
	public void createDivision(CommandArguments arguments)
	{
		logger.info("Division Created: " + arguments.getArgument("Name") 
		+ ", in Tournament: " + arguments.getArgument("Tournament"));
	}
	
	public void addVenue(CommandArguments arguments)
	{
		logger.info("Venue added: " + arguments.getArgument("LocationName") 
		+ ", to Tournament: " + arguments.getArgument("TournamentName"));

	}
	
	public void addPlayer(CommandArguments arguments)
	{
		logger.info("Player added: " + arguments.getArgument("Player1Name")
		+ ", to Division: " + arguments.getArgument("DivisionName")
		+ ", in Tournament: " + arguments.getArgument("TournamentName"));
	}
	
	public void createMatch(CommandArguments arguments)
	{
		logger.info("Match created: " + arguments.getArgument("MatchNumber")
		+ ", to Division: " + arguments.getArgument("DivisionName")
		+ ", in Tournament: " + arguments.getArgument("TournamentName"));
	}
	
	public void createGame(CommandArguments arguments)
	{
		logger.info("Game created: " + arguments.getArgument("GameNumber")
		+ ", to Match: " + arguments.getArgument("MatchNumber")
		+ ", in Division: " + arguments.getArgument("DivisionName")
		+ ", in Tournament: " + arguments.getArgument("TournamentName"));
	}

	public void createFoul(CommandArguments arguments)
	{
		logger.info("Foul Created: " + arguments.getArgument("GameNumber")
		+ ", to Game: " + arguments.getArgument("GameNumber")
		+ ", in Match: " + arguments.getArgument("MatchNumber")
		+ ", in Division: " + arguments.getArgument("DivisionName")
		+ ", in Tournament: " + arguments.getArgument("TournamentName"));
	}
}