package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.Utilities.CommandArguments;
import com.google.gson.JsonObject;

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

    private ResultSet rs;
    private PreparedStatement st;

	public EndpointAPI()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (InstantiationException e)
		{
			logger.error(e);
		}
		catch (IllegalAccessException e)
		{
			logger.error(e);
		}
		catch (ClassNotFoundException e)
		{
			logger.error(e);
		}
	}


	public String createUser(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `pid_person`) VALUES (?, ?, CAST(CURRENT_TIMESTAMP AS DATE), ?, ?);");
	        st.setString(1, (String)arguments.getArgument("UserName"));
	        st.setString(2, (String)arguments.getArgument("Password"));
            st.setString(3, (String)arguments.getArgument("Permissions"));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("User Created: " + arguments.getArgument("Username"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createUser:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createUser:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}


	public String createPerson(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `person` (`name`, `email`, `city`, `state`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `lid_homeClub`) VALUES (?, ?, null, null, ?, ?, ?, null, null, null, null, null);");
	        st.setString(1, (String)arguments.getArgument("PersonName"));
	        st.setString(2, (String)arguments.getArgument("Email"));
            st.setString(3, (String)arguments.getArgument("Phone"));
            st.setString(4, (String)arguments.getArgument("Gender"));
            st.setDate(5, java.sql.Date.valueOf((String)arguments.getArgument("Birthdate")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Person Created: " + arguments.getArgument("PersonName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createPerson:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createPerson:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	
	public String createCourt(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `court` (`courtName`, `lid_location`) VALUES ('A', @bac);");
	        st.setString(1, (String)arguments.getArgument("CourtName"));
	        st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("LocationID")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Court Created: " + arguments.getArgument("CourtName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createCourt:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createCourt:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	
	public String createLocation(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `location` (`name`, `address`, `city`, `state`, `zip`, `phone`, `weekdayOpenTime`, `weekdayCloseTime`, `weekendOpenTime`, `weekendCloseTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	        st.setString(1, (String)arguments.getArgument("LocationName"));
	        st.setString(2, (String)arguments.getArgument("Address"));
            st.setString(3, (String)arguments.getArgument("City"));
            st.setString(4, (String)arguments.getArgument("State"));
            st.setString(5, (String)arguments.getArgument("Zip"));
            st.setString(6, (String)arguments.getArgument("Phone"));
            st.setTime(7, java.sql.Time.valueOf((String)arguments.getArgument("WeekdayOpenTime")));
            st.setTime(8, java.sql.Time.valueOf((String)arguments.getArgument("WeekdayCloseTime")));
            st.setTime(9, java.sql.Time.valueOf((String)arguments.getArgument("WeekendOpenTime")));
            st.setTime(10, java.sql.Time.valueOf((String)arguments.getArgument("WeekendCloseTime")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Location Created: " + arguments.getArgument("LocationName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createLocation:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createLocation:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}


	public String createTournament(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `uid_owner`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
	        st.setString(1, (String)arguments.getArgument("TournamentName"));
            st.setDate(2, java.sql.Date.valueOf((String)arguments.getArgument("StartDate")));
            st.setDate(3, java.sql.Date.valueOf((String)arguments.getArgument("EndDate")));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("IsGuestViewable")));
            st.setInt(5, java.lang.Integer.valueOf((String)arguments.getArgument("TravelTime")));
            st.setTime(6, java.sql.Time.valueOf((String)arguments.getArgument("StartTimeWeekdays")));
            st.setTime(7, java.sql.Time.valueOf((String)arguments.getArgument("EndTimeWeekdays")));
            st.setTime(8, java.sql.Time.valueOf((String)arguments.getArgument("StartTimeWeekends")));
            st.setTime(9, java.sql.Time.valueOf((String)arguments.getArgument("EndTimeWeekends")));
            st.setInt(10, java.lang.Integer.valueOf((String)arguments.getArgument("MaxDivPerPlayer")));
            st.setInt(11, java.lang.Integer.valueOf((String)arguments.getArgument("OwnerUserID")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Tournament Created: " + arguments.getArgument("TournamentName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createTournament:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createTournament:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	
	public String getPersonID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `pid` FROM `person` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("PersonName"));
	        rs = st.executeQuery();
	        rs.next();
	        int pid = Integer.parseInt(rs.getString(1));

            logger.info("Selected pid from Person: " + arguments.getArgument("PersonName"));

            JsonObject e = new JsonObject();
            e.addProperty("pid", pid);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting pid: " + arguments.getArgument("PersonName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            e.addProperty("reason", "SQL Exception");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting pid: " + arguments.getArgument("PersonName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            e.addProperty("reason", "Java Exception");
            return e.toString();
        }
	}
	
	public String getUserID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `uid` FROM `user` WHERE `username` = ?;");
            st.setString(1, (String)arguments.getArgument("UserName"));
	        rs = st.executeQuery();
	        rs.next();
	        int uid = Integer.parseInt(rs.getString(1));

            logger.info("Selected uid from user: " + arguments.getArgument("UserName"));

            JsonObject e = new JsonObject();
            e.addProperty("uid", uid);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting uid: " + arguments.getArgument("UserName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting uid: " + arguments.getArgument("UserName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
}
