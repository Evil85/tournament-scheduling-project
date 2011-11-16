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
	
	//Get whole tuple by id for table type
	public String getTupleByID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT * FROM `person` WHERE `id` = ?;");
//            st.setString(1, (String)arguments.getArgument("TableName"));
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("ID")));
	        rs = st.executeQuery();
	        rs.next();
	        int size = rs.getMetaData().getColumnCount();

            JsonObject e = new JsonObject();
//            e.addProperty("colsize", size);
                   
	        for (int i = 0; i < size; i++)
	        {
//	            logger.info("colname: " + rs.getMetaData().getColumnName(i) + ", coldata: " + rs.getString(i));
                e.addProperty(rs.getMetaData().getColumnName(i), (String)rs.getObject(i));
            }

            logger.info("Selected row from table: " + arguments.getArgument("TableName"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting row from table: " + arguments.getArgument("TableName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting row from table: " + arguments.getArgument("TableName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}


    //Getters and setters for USER: createUser, getUserID

	public String createUser(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `id_person`) VALUES (?, ?, CAST(CURRENT_TIMESTAMP AS DATE), ?, ?);");
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
	
	public String getUserID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `user` WHERE `username` = ?;");
            st.setString(1, (String)arguments.getArgument("UserName"));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected uid from user: " + arguments.getArgument("UserName"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
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

    //Getters and setters for PERSON: createPerson, getPersonID

	public String createPerson(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `person` (`name`, `email`, `city`, `state`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `id_homeClub`) VALUES (?, ?, null, null, ?, ?, ?, null, null, null, null, null);");
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
	
	public String getPersonID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `person` WHERE `email` = ?;");
            st.setString(1, (String)arguments.getArgument("PersonEmail"));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected pid from Person: " + arguments.getArgument("PersonEmail"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting pid: " + arguments.getArgument("PersonEmail"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting pid: " + arguments.getArgument("PersonEmail"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for COURT: createCourt, getCourtID

	public String createCourt(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `court` (`courtName`, `id_location`) VALUES ('A', @bac);");
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
	
	public String getCourtID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `court` WHERE `courtName` = ? AND `id_location` = ? ;");
            st.setString(1, (String)arguments.getArgument("CourtName"));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("LocationID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected cid from Court: " + arguments.getArgument("CourtName"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting cid: " + arguments.getArgument("CourtName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting cid: " + arguments.getArgument("CourtName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for LOCATION: createLocation, getLocationID
	
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
	
	public String getLocationID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `location` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("LocationName"));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected lid from Location: " + arguments.getArgument("LocationName"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting lid: " + arguments.getArgument("LocationName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting lid: " + arguments.getArgument("LocationName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

    //Getters and setters for TOURNAMENT: createTournament, getTournamentID

	public String createTournament(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `id_owner`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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
	
	public String getTournamentID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `tournament` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("TournamentName"));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected tid from tournament: " + arguments.getArgument("TournamentName"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting tid: " + arguments.getArgument("TournamentName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting tid: " + arguments.getArgument("TournamentName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for VENUE: addVenue
	
	public String addVenue(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `venue` (`id_location`, `id_tournament`) VALUES (?, ?);");

            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("LocationID")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Venue Added to location lid: " + arguments.getArgument("LocationID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during addVenue:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during addVenue:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for DIVISION: createDivision, getDivisionID
	
    public String createDivision(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `division` (`name`, `isDouble`, `estTime`, `genderConstraint`, `minAge`, `maxAge`, `tournType`, `id_tournament`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
	        st.setString(1, (String)arguments.getArgument("Name"));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("IsDouble")));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("EstTime")));
            st.setString(4, (String)arguments.getArgument("GenderConstraint"));
            st.setInt(5, java.lang.Integer.valueOf((String)arguments.getArgument("MinAge")));
            st.setInt(6, java.lang.Integer.valueOf((String)arguments.getArgument("MaxAge")));
            st.setString(7, (String)arguments.getArgument("TournamentType"));
            st.setInt(8, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
            
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Division Created: " + arguments.getArgument("Name"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createDivision:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createDivision:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	public String getDivisionID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `division` WHERE `name` = ? AND `id_tournament` = ? ;");
            st.setString(1, (String)arguments.getArgument("DivisionName"));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected did from division: " + arguments.getArgument("DivisionName"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting did: " + arguments.getArgument("DivisionName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting did: " + arguments.getArgument("DivisionName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for PLAYER: addPlayer, getPlayerID

    public String addPlayer(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);
            logger.info("starting addPlayer");
            st = conn.prepareStatement("SELECT `isDouble` FROM `division` WHERE `id` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
	        rs = st.executeQuery();
	        rs.next();
	        int isDouble = Integer.parseInt(rs.getString(1));
	        logger.info("isDouble? " + isDouble);
            
            st = conn.prepareStatement ("INSERT INTO `player` (`id_player1`, `id_player2`, `id_division`) VALUES (?, ?, ?);");
     
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("Player1ID")));

            if (isDouble == 1)
                st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("Player2ID")));
            else
                st.setNull(2, java.sql.Types.INTEGER);

            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));

            st.executeUpdate();
            conn.close();
	        	        
	        logger.info("Player added (p1)pid: " + arguments.getArgument("Player1ID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during addPlayer:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during addPlayer:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	public String getPlayerID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `player` WHERE `id_player1` = ? AND `id_division` = ? ;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("Player1ID")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected pid from player with player1 pid: " + arguments.getArgument("Player1ID"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting plid: " + arguments.getArgument("Player1ID"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting plid: " + arguments.getArgument("Player1ID"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for MATCH: createMatch, getMatchID
	
	public String createMatch(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement ("INSERT INTO `match` (`startTime`, `matchNumber`, `id_division`, `id_player1`, `id_player2`, `id_court`) VALUES (?, ?, ?, ?, ?, ?);");
            
            st.setTimestamp(1, java.sql.Timestamp.valueOf((String)arguments.getArgument("StartTime")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("MatchNumber")));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("Player1ID")));
            st.setInt(5, java.lang.Integer.valueOf((String)arguments.getArgument("Player2ID")));
            st.setInt(6, java.lang.Integer.valueOf((String)arguments.getArgument("CourtID")));
            
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Match created matchno: " + arguments.getArgument("MatchNumber"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createMatch:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createMatch:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");

            return e.toString();
        }
	}
	
	public String getMatchID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `match` WHERE `matchNumber` = ? AND `id_division` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("MatchNumber")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected mid from match num: " + arguments.getArgument("MatchNumber"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting mid: " + arguments.getArgument("MatchNumber"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting mid: " + arguments.getArgument("MatchNumber"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	
    //Getters and setters for GAME: createGame, getGameID
	
	public String createGame(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement ("INSERT INTO `game` (`gameNumber`, `team1Score`, `team2Score`, `id_match`) VALUES (?, 0, 0, ?);");
            
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("GameNumber")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("MatchID")));
                        
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Game created for matchID: " + arguments.getArgument("MatchID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createGame:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createGame:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
	public String getGameID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `id` FROM `game` WHERE `gameNumber` = ? AND `id_match` = ? ;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("GameNumber")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("MatchID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.info("Selected gid from game: " + arguments.getArgument("GameNumber"));

            JsonObject e = new JsonObject();
            e.addProperty("id", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting gid: " + arguments.getArgument("GameNumber"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting gid: " + arguments.getArgument("GameNumber"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
	
    //Getters and setters for FOUL: createFoul
	
	public String createFoul(CommandArguments arguments)
	{
        try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

	        st = conn.prepareStatement ("INSERT INTO `foul` (`foulName`, `penalty`, `foulTime`, `id_game`, `id_committer`) VALUES (?, ?, ?, ?, ?);");
	        st.setString(1, (String)arguments.getArgument("FoulName"));
   	        st.setString(2, (String)arguments.getArgument("Penalty"));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("FoulTime")));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("GameID")));
            st.setInt(5, java.lang.Integer.valueOf((String)arguments.getArgument("CommitterPlayerID")));
            
            st.executeUpdate();
	        conn.close();
	        
    		logger.info("Foul Created: " + arguments.getArgument("FoulName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", "true");
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during createFoul:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during createFoul:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
}
