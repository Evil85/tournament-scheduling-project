package com;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.lang.Integer;
import org.apache.log4j.Logger;
import com.Utilities.CommandArguments;
import java.sql.*;
import java.sql.DriverManager;
import com.Server.HttpExchange;
import com.Server.HttpServer;
import com.Server.IHttpHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
    private static final String URL = "jdbc:mysql://srproj.cs.wwu.edu:3306/tourn_201140";
    private static final String user = "admtourn201140";
    private static final String pass = "yinvamOph";
    
    private PreparedStatement st;
    
	public JsonObject createUser(CommandArguments arguments)
	{
        try {
            Class.forName("mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, user, pass);
	
	        st = conn.prepareStatement("SELECT `pid` FROM `person` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("PersonName"));
	        ResultSet rs = st.executeQuery();
	        rs.next();
	        int pid = Integer.parseInt(rs.getString(1));

	        st = conn.prepareStatement ("INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `pid_person`) VALUES (?, ?, CAST(CURRENT_TIMESTAMP AS DATE), ?, ?");
	        
	        st.setString(1, (String)arguments.getArgument("UserName"));
	        st.setString(2, (String)arguments.getArgument("Password"));
            st.setString(3, (String)arguments.getArgument("Permissions"));
	        st.setInt(4, pid);
	        
            st.executeUpdate();
            conn.commit();
	        conn.close();
	
    		logger.info("User Created: " + arguments.getArgument("Username"));
	        
	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createUser:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createUser:\n" + ex);
	        
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
	}
	
	public JsonObject createPerson(CommandArguments arguments)
	{
	    try {
            Class.forName("mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `person` (`name`, `email`, `address`, `phone`, `gender`, `birthdate`) VALUES (?, ?, ?, ?, ?, ?)");
	        
	        st.setString(1, (String) arguments.getArgument("PersonName"));
	        st.setString(2, (String) arguments.getArgument("Email"));
            st.setString(3, (String) arguments.getArgument("Address"));
            st.setString(4, (String) arguments.getArgument("Phone"));
            st.setString(5, (String) arguments.getArgument("Gender"));
            st.setDate(6, java.sql.Date.valueOf((String)arguments.getArgument("Birthday")));
	        
            st.executeUpdate();
            conn.commit();
	        conn.close();

    		logger.info("Person Created: " + arguments.getArgument("Name"));
            
            JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e;
		}
		catch (SQLException ex)
		{
	        logger.error("SQL Exception while creating person: " + arguments.getArgument("PersonName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
		}
		catch (Exception ex)
		{
			logger.error("Java Exception while creating person: " + arguments.getArgument("PersonName"));
			
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
		}
	}
	
	public JsonObject createLocation(CommandArguments arguments)
	{
	    try {
    	    Class.forName("mysql.jdbc.Driver");
	        Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `location` (`name`, `address`, `city`, `state`, `zip`, `phone`, `weekdayOpenTime`, `weekdayCloseTime`, `weekendOpenTime`, `weekendCloseTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	        
	        st.setString(1, (String) arguments.getArgument("LocName"));
	        st.setString(2, (String) arguments.getArgument("Address"));
            st.setString(3, (String) arguments.getArgument("City"));
            st.setString(4, (String) arguments.getArgument("State"));
            st.setString(5, (String) arguments.getArgument("Zip"));
            st.setString(6, (String) arguments.getArgument("Phone"));
            st.setDate(7, java.sql.Date.valueOf((String)arguments.getArgument("WeekdayOpenTime")));
            st.setDate(8, java.sql.Date.valueOf((String)arguments.getArgument("WeekdayCloseTime")));
            st.setDate(9, java.sql.Date.valueOf((String)arguments.getArgument("WeekendOpenTime")));
            st.setDate(10, java.sql.Date.valueOf((String)arguments.getArgument("WeekendCloseTime")));
          
            st.executeUpdate();
            conn.commit();
	        conn.close();

    		logger.info("Location Created: " + arguments.getArgument("Name"));
    		
            JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while creating location: " + arguments.getArgument("LocName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while creating location: " + arguments.getArgument("LocName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
	}
	
	public JsonObject createCourt(CommandArguments arguments)
	{
	    try {
    	    Class.forName("mysql.jdbc.Driver");
	        Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `lid` FROM `location` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("LocName"));
	        ResultSet rs = st.executeQuery();
	        rs.next();
	        int lid = Integer.parseInt(rs.getString(1));

	        st = conn.prepareStatement ("INSERT INTO `court` (`courtName`, `lid_location`) VALUES (?, ?");
	        
	        st.setString(1, (String)arguments.getArgument("CourtName"));
	        st.setInt(2, lid);
	        
            st.executeUpdate();
            conn.commit();
	        conn.close();

    		logger.info("Court Created: " + arguments.getArgument("Name"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while creating court: " + arguments.getArgument("CourtName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while creating court: " + arguments.getArgument("CourtName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        
	}
	
	public JsonObject createTournament(CommandArguments arguments)
	{
	    try {
    	    Class.forName("mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `uid` FROM `user` WHERE `username` = ?;");
            st.setString(1, (String)arguments.getArgument("UserName"));
	        ResultSet rs = st.executeQuery();
	        rs.next();
	        int uid = Integer.parseInt(rs.getString(1));

	        st = conn.prepareStatement ("INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `uid_owner`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
	        
	        st.setString(1, (String)arguments.getArgument("TournamentName"));
	        st.setDate(2, java.sql.Date.valueOf((String)arguments.getArgument("StartDate")));
	        st.setDate(3, java.sql.Date.valueOf((String)arguments.getArgument("EndDate")));
	        st.setInt(4, java.lang.Integer.valueOf((String) arguments.getArgument("GuestViewable")));
	        st.setInt(5, java.lang.Integer.valueOf((String) arguments.getArgument("TravelTime")));
            st.setTime(6, java.sql.Time.valueOf((String)arguments.getArgument("StartTimeWeekdays")));
            st.setTime(7, java.sql.Time.valueOf((String)arguments.getArgument("EndTimeWeekdays")));
            st.setTime(8, java.sql.Time.valueOf((String)arguments.getArgument("StartTimeWeekends")));
            st.setTime(9, java.sql.Time.valueOf((String)arguments.getArgument("EndTimeWeekends")));

            st.executeUpdate();
            conn.commit();
	        conn.close();

            logger.info("Tournament Created: " + arguments.getArgument("Name"));
            
            JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while creating location: " + arguments.getArgument("TournamentName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while creating location: " + arguments.getArgument("TournamentName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
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
