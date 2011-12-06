package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.Utilities.CommandArguments;
import com.google.gson.JsonObject;

//import java.util.concurrent.ConcurrentSkipListSet;

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

	public boolean checkString(String s)
	{
	    int length = s.length();
        for (int i = 0; i < length; i++)
        {
            char c = s.charAt(i);
            if (!Character.isLetter(c) && (!Character.isDigit(c)) && (c != '_'))
                return false;
        }
        return true;
	}
/*
	//Schedule tournament
	public String scheduleTournament(CommandArguments arguments)
	{
	    try {
	    
	        int tournamentID = java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID"));
	    
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT * FROM `tournament` WHERE `id` = ?;");
            st.setInt(1, tournamentID);
            rs = st.executeQuery();
            rs.next();

            String tStart = rs.getString("start_date");
            String tEnd = rs.getString("end_date");

            String std = rs.getString("start_time_weekdays");
            String etd = rs.getString("end_time_weekdays");
            String ste = rs.getString("start_time_weekends");
            String ete = rs.getString("end_time_weekends");

            SortedSet<TimeSpan> tournTimes = buildAvailability(tStart, tEnd, std, etd, ste, ete);

            st = conn.prepareStatement("SELECT DISTINCT `person`.`id`, `person`.`name`, `person`.`unavailTimeStart1`, `person`.`unavailTimeEnd1`, `person`.`unavailTimeStart2`, `person`.`unavailTimeEnd2` FROM `person`, `player`, `division` WHERE (`person`.`id` = `player`.`id_player1` OR `person`.`id` = `player`.`id_player2`) AND `player`.`id_division` = `division`.`id` AND `division`.`id_tournament` = ?;");
		    st.setInt(1, tournamentID);
		    rs = st.executeQuery();

		    Map<Integer, Player> players = new HashMap<Integer, Player>();

		    while (rs.next())
		    {
			    Integer id = rs.getInt("id");

			    SortedSet<TimeSpan> playerTimes = new TreeSet<TimeSpan>(tournTimes);

			    Calendar cal = Calendar.getInstance();
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			    if (rs.getString("unavailTimeStart1") != null)
			    {
					cal.setTime(dateFormat.parse(rs.getString("unavailTimeStart1")));
					Timestamp start1 = new Timestamp(cal.getTime().getTime());
					cal.setTime(dateFormat.parse(rs.getString("unavailTimeEnd1")));
					Timestamp end1 = new Timestamp(cal.getTime().getTime());
					SchedulingUtil.RemoveAvailability(playerTimes, new TimeSpan(start1, end1));
			    }

			    if (rs.getString("unavailTimeStart2") != null)
			    {
					cal.setTime(dateFormat.parse(rs.getString("unavailTimeStart2")));
					Timestamp start2 = new Timestamp(cal.getTime().getTime());
					cal.setTime(dateFormat.parse(rs.getString("unavailTimeEnd2")));
					Timestamp end2 = new Timestamp(cal.getTime().getTime());
					SchedulingUtil.RemoveAvailability(playerTimes, new TimeSpan(start2, end2));
			    }

              players.put(id, new Player(id, rs.getString("name"), playerTimes));
		    }

		    st = conn.prepareStatement("SELECT DISTINCT `court`.`id` courtID, `location`.`weekdayOpenTime`, `location`.`weekdayCloseTime`, `location`.`weekendOpenTime`, `location`.`weekendCloseTime`, `location`.`id` locationID FROM `court`, `location`, `venue` WHERE `venue`.`id_tournament` = ? AND `venue`.`id_location` = `court`.`id_location` AND `venue`.`id_location` = `location`.`id`;");

		    st.setInt(1, tournamentID);
		    rs = st.executeQuery();

		    List<Court> courts = new ArrayList<Court>();

		    // Set up the courts from the sql query.
		    while(rs.next())
		    {
			    String weekdayOpenTime = rs.getString("weekdayOpenTime");
			    String weekdayCloseTime = rs.getString("weekdayCloseTime");
			    String weekendOpenTime = rs.getString("weekendOpenTime");
			    String weekendCloseTime = rs.getString("weekendCloseTime");

			    SortedSet<TimeSpan> courtTimes = buildAvailability(tStart, tEnd, weekdayOpenTime, weekdayCloseTime, weekendOpenTime, weekendCloseTime);
			    Court court = new Court(rs.getInt("courtID"), "TestCourt", rs.getString("locationID"), SchedulingUtil.IntersectAvailability(tournTimes, courtTimes));
			    courts.add(court);
		    }
		    CourtManager courtManager = new CourtManager(courts);
		    

            // gets teamIDs, PlayerIDs, for a Division ID - use resultant divison IDs from last query
            st = conn.prepareStatement("SELECT DISTINCT team.`id_player1` as Player1ID, team.`id_player2` as Player2ID, d.`id` as DivisionID, team.`id` as TeamID FROM `player` team WHERE team.id_division = d.id AND d.id_tournament = ?;");
		    st.setInt(1, tournamentID);
		    rs = st.executeQuery();
            Map<Integer, List<Team>> divisionTeams = new HashMap<Integer, List<Team>>();
            
            while (rs.next())
            {
                int DivisionID = rs.getInt("DivisionID");
                List<Player> t = new ArrayList<Player>();
                t.add(players.get(rs.getInt("Player1ID")));
                t.add(players.get(rs.getInt("Player2ID")));
                if (!divisionTeams.containsKey(DivisionID))
                    divisionTeams.put(DivisionID, new ArrayList<Team>());
                
                divisionTeams.get(DivisionID).add(new Team(rs.getString("TeamID"), t));
            }

            
            // gets Division data for a Tournament ID
            st = conn.prepareStatement("SELECT DISTINCT d.id, d.name, d.estTime, d.tournType FROM `division` d team WHERE d.id_tournament = ?;");
		    st.setInt(1, tournamentID);
		    rs = st.executeQuery();
		    
		    List<Division> divisions = new ArrayList<Division>();
            while (rs.next())
            {
                String tournType = rs.getString("tournType");
                Division div;
                if (tournType.equals("round robin"))
                    div = new RoundRobin(rs.getInt("id"), rs.getString("name"), rs.getInt("estTime"), courtManager, divisionTeams.get(rs.getInt("id")));
                else
                    div = new PoolPlay(rs.getInt("id"), rs.getString("name"), rs.getInt("estTime"), courtManager, divisionTeams.get(rs.getInt("id")));
                
                divisions.add(div);
            }

            Tournament tourn = new Tournament(tournamentID, divisions);

            int rowSize = 0;
            JsonObject e = new JsonObject();

	        e.addProperty("result", tourn.toString());

            logger.trace("Scheduled tournament: " + (String)arguments.getArgument("TournamentID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while scheduling tournament: " + arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while scheduling tournament: " + arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}
*/
	private static SortedSet<TimeSpan> buildAvailability(String tStart, String tEnd, String std, String etd, String ste, String ete) throws java.text.ParseException
	{
	    SortedSet<TimeSpan> times = new TreeSet<TimeSpan>();
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        cal.setTime(dateFormat.parse(tStart));
        java.util.Date endDate = dateFormat.parse(tEnd);
        while (!cal.getTime().after(endDate))
        {
            String startTimes[];
            String endTimes[];

            if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY))
            {
                startTimes = ste.split(":", 3);
                endTimes = ete.split(":", 3);
            }
            else
            {
                startTimes = std.split(":", 3);
                endTimes = etd.split(":", 3);
            }


            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimes[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(startTimes[1]));
            Timestamp start = new Timestamp(cal.getTime().getTime());

            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTimes[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(endTimes[1]));
            Timestamp end = new Timestamp(cal.getTime().getTime());

            times.add(new TimeSpan(start, end));

            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
        }

	    return times;
	}

	//Get whole tuple by id for table type (user table will NOT send back passwords)
	public String getTupleByID(CommandArguments arguments)
	{
	    try {
            String name = (String)arguments.getArgument("TableName");
            if (!checkString(name))
                throw new Exception("Possible SQL attack!");

            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT * FROM `" + name + "` WHERE `id` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("ID")));

	        rs = st.executeQuery();
	        rs.next();
	        int size = rs.getMetaData().getColumnCount();

            JsonObject e = new JsonObject();

	        for (int i = 1; i < size+1; i++)
	        {
	            if (!(rs.getMetaData().getColumnName(i).equalsIgnoreCase("password")))
                    e.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
            }

            logger.trace("Selected row from table: " + name);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting row from table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting row from table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	//Get limited sorted table by name (user table will NOT send back passwords)
	public String getTableOrderLimitSpecify(CommandArguments arguments)
	{
	    try {
	        String name = (String)arguments.getArgument("TableName");
            if (!checkString(name))
                throw new Exception("Possible SQL attack!");

            String sqlStr = "select * from `" + name + "` ";

  	        if (arguments.doesKeyExist("SpecColumn"))
  	        {
	            String spec = (String)arguments.getArgument("SpecColumn");
                if (!checkString(spec))
                    throw new Exception("Possible SQL attack!");

	            sqlStr += "where `" + spec + "` = ? ";
	        }

	        if (arguments.doesKeyExist("OrderColumn"))
  	        {
	            String order = (String)arguments.getArgument("OrderColumn");
                if (!checkString(order))
                    throw new Exception("Possible SQL attack!");

	            sqlStr += "order by `" + order + "` ";
	        }

	        if (arguments.doesKeyExist("SkipCount"))
  	        {
	            sqlStr = sqlStr + "limit " + java.lang.Integer.valueOf((String)arguments.getArgument("SkipCount")) + ", " + java.lang.Integer.valueOf((String)arguments.getArgument("GetCount"));
	        }

	        sqlStr += ";";


            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement(sqlStr);

            if (arguments.doesKeyExist("SpecValue"))
                st.setString(1, (String)arguments.getArgument("SpecValue"));

            logger.trace("sql statement: " + st);

	        rs = st.executeQuery();
            int rowSize = 0;
			int colSize = rs.getMetaData().getColumnCount();
			JsonObject e = new JsonObject();
			while(rs.next())
			{
			    JsonObject m = new JsonObject();
			    for (int i = 1; i < colSize+1; i++)
                {
                    if (!(rs.getMetaData().getColumnName(i).equalsIgnoreCase("password")))
                        m.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                e.addProperty(java.lang.String.valueOf(rowSize++), m.toString());
			}

            logger.debug("Selected limited sorted table: " + name);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting limited sorted table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting limited sorted table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}



	//Get table count by name
	public String getTableCountByName(CommandArguments arguments)
	{
	    try {
	        String name = (String)arguments.getArgument("TableName");
            if (!checkString(name))
                throw new Exception("Possible SQL attack!");

            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT count(`id`) FROM `" + name + "`;");
	        rs = st.executeQuery();
	        rs.next();
	        int count = Integer.parseInt(rs.getString(1));

			JsonObject e = new JsonObject();
            e.addProperty("result", count);
            logger.debug("Selected count on table: " + name);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting count on table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting count on table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	//Get count by from any table with one column also specified
	public String getCountByValue(CommandArguments arguments)
	{
	    try {
	        String name = (String)arguments.getArgument("TableName");
            if (!checkString(name))
                throw new Exception("Possible SQL attack!");

	        String column = (String)arguments.getArgument("ColumnName");
            if (!checkString(column))
                throw new Exception("Possible SQL attack!");

            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("select count(*) as count from `" + name +"` where `" + column +"` = ?;");
            st.setString(1, (String)arguments.getArgument("ColumnValue"));

            rs = st.executeQuery();
	        rs.next();
	        int count = Integer.parseInt(rs.getString(1));

			JsonObject e = new JsonObject();
            e.addProperty("result", count);
            logger.debug("Selected count on table: " + name);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting count on table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");

            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting count on table: " + (String)arguments.getArgument("TableName"));
	        logger.error("error: " + ex);
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

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("User Created: " + arguments.getArgument("Username"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

            logger.debug("Selected uid from user: " + arguments.getArgument("UserName"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting uid: " + arguments.getArgument("UserName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting uid: " + arguments.getArgument("UserName"));
	        logger.error("error: " + ex);
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

	        st = conn.prepareStatement ("INSERT INTO `person` (`name`, `email`, `city`, `state`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `id_homeClub`) VALUES (?, ?, ?, ?, ?, ?, ?, null, null, null, null, ?);");
	        st.setString(1, (String)arguments.getArgument("PersonName"));
	        st.setString(2, (String)arguments.getArgument("Email"));

	        if (arguments.doesKeyExist("City"))
	            st.setString(3, (String)arguments.getArgument("City"));
            else
                st.setNull(3, java.sql.Types.VARCHAR);

	        if (arguments.doesKeyExist("State"))
	            st.setString(4, (String)arguments.getArgument("State"));
            else
                st.setNull(4, java.sql.Types.VARCHAR);


            st.setString(5, (String)arguments.getArgument("Phone"));
            st.setString(6, (String)arguments.getArgument("Gender"));
            st.setDate(7, java.sql.Date.valueOf((String)arguments.getArgument("Birthdate")));

	        if (arguments.doesKeyExist("HomeClubID"))
	            st.setInt(8, java.lang.Integer.valueOf((String)arguments.getArgument("HomeClubID")));
            else
                st.setNull(8, java.sql.Types.INTEGER);

            st.executeUpdate();

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Person Created: " + arguments.getArgument("PersonName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

	public String updatePersonAvailability(CommandArguments arguments)
	{
	try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement ("UPDATE `person` SET `unavailTimeStart1` = ?, SET `unavailTimeEnd1` = ?, `unavailTimeStart2` = ?, `unavailTimeEnd2` = ? WHERE `id` = ?;");

	        if (arguments.doesKeyExist("UnavailStart1"))
                st.setDate(1, java.sql.Date.valueOf((String)arguments.getArgument("UnavailStart1")));
            else
                st.setNull(1, java.sql.Types.DATE);

	        if (arguments.doesKeyExist("UnavailEnd1"))
                st.setDate(2, java.sql.Date.valueOf((String)arguments.getArgument("UnavailEnd1")));
            else
                st.setNull(2, java.sql.Types.DATE);

	        if (arguments.doesKeyExist("UnavailStart2"))
                st.setDate(3, java.sql.Date.valueOf((String)arguments.getArgument("UnavailStart2")));
            else
                st.setNull(3, java.sql.Types.DATE);

	        if (arguments.doesKeyExist("UnavailEnd2"))
                st.setDate(4, java.sql.Date.valueOf((String)arguments.getArgument("UnavailEnd2")));
            else
                st.setNull(4, java.sql.Types.DATE);

            st.setInt(5, java.lang.Integer.valueOf((String) arguments.getArgument("PersonID")));

            st.executeUpdate();

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Person: " + arguments.getArgument("PersonID") + " unavailTimes updated");

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during updatePersonAvail:\n" + ex);

	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during updatePersonAvail:\n" + ex);

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

            logger.debug("Selected pid from Person: " + arguments.getArgument("PersonEmail"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting pid: " + arguments.getArgument("PersonEmail"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting pid: " + arguments.getArgument("PersonEmail"));
	        logger.error("error: " + ex);
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

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Court Created: " + arguments.getArgument("CourtName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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
            st.setString(1, (String) arguments.getArgument("CourtName"));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("LocationID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.debug("Selected cid from Court: " + arguments.getArgument("CourtName"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting cid: " + arguments.getArgument("CourtName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting cid: " + arguments.getArgument("CourtName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	public String getCourtsByLocationID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT * FROM `court` WHERE `id_location` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("LocationID")));
	        rs = st.executeQuery();

			int colSize = rs.getMetaData().getColumnCount();
			JsonObject e = new JsonObject();
			int rowSize = 0;
			while(rs.next())
			{
			    JsonObject m = new JsonObject();
			    for (int i = 1; i < colSize+1; i++)
                {
                    m.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                e.addProperty(java.lang.String.valueOf(rowSize++), m.toString());
			}

            logger.debug("Selected courts by locationID: " + arguments.getArgument("LocationID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting courts by locationID: " + arguments.getArgument("LocationID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting courts by locationID: " + arguments.getArgument("LocationID"));
	        logger.error("error: " + ex);
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

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Location Created: " + arguments.getArgument("LocationName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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
            st.setString(1, (String) arguments.getArgument("LocationName"));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.debug("Selected lid from Location: " + arguments.getArgument("LocationName"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting lid: " + arguments.getArgument("LocationName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting lid: " + arguments.getArgument("LocationName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	public String getLocationsByTournamentID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT * FROM `location`, `venue` WHERE `location`.`id`=`venue`.`id_location` AND `venue`.`id_tournament` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
	        rs = st.executeQuery();

			int colSize = rs.getMetaData().getColumnCount();
			JsonObject e = new JsonObject();
			int rowSize = 0;
			while(rs.next())
			{
			    JsonObject m = new JsonObject();
			    for (int i = 1; i < colSize+1; i++)
                {
                    m.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                e.addProperty(java.lang.String.valueOf(rowSize++), m.toString());
			}

            logger.debug("Selected location (venue) by TournamentID: " + arguments.getArgument("TournamentID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting location (venue) by TournamentID: " + arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting location (venue) by TournamentID: " + arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
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

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Tournament Created: " + arguments.getArgument("TournamentName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

            logger.debug("Selected tid from tournament: " + arguments.getArgument("TournamentName"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting tid: " + arguments.getArgument("TournamentName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting tid: " + arguments.getArgument("TournamentName"));
	        logger.error("error: " + ex);
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

	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Venue Added to location lid: " + arguments.getArgument("LocationID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

            if (arguments.doesKeyExist("GenderConstraint"))
                st.setString(4, (String)arguments.getArgument("GenderConstraint"));
            else
                st.setNull(4, java.sql.Types.VARCHAR);


            if (arguments.doesKeyExist("MinAge"))
                st.setInt(5, java.lang.Integer.valueOf((String)arguments.getArgument("MinAge")));
            else
                st.setNull(5, java.sql.Types.INTEGER);

            if (arguments.doesKeyExist("MaxAge"))
                st.setInt(6, java.lang.Integer.valueOf((String)arguments.getArgument("MaxAge")));
            else
                st.setNull(6, java.sql.Types.VARCHAR);

            st.setString(7, (String)arguments.getArgument("TournamentType"));
            st.setInt(8, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));

            st.executeUpdate();

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Division Created: " + arguments.getArgument("Name"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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
            st.setString(1, (String) arguments.getArgument("DivisionName"));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
	        rs = st.executeQuery();
	        rs.next();
	        int id = Integer.parseInt(rs.getString(1));

            logger.debug("Selected did from division: " + arguments.getArgument("DivisionName"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting did: " + arguments.getArgument("DivisionName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting did: " + arguments.getArgument("DivisionName"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	public String getDivisionListForPlayerTourn(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("select d.*, p.players, pl.plid from division d left join (select count(*) as players, id_division as did from player where id_division in (select id from division where id_tournament = ?) group by did) as p on p.did = d.id left join (select id as plid, id_division as did from player where (id_player1 = ? or id_player2 = ?)) pl on d.id = pl.did where d.id_tournament = ? order by d.id desc limit " +
            java.lang.Integer.valueOf((String)arguments.getArgument("SkipCount")) + ", "+
            java.lang.Integer.valueOf((String)arguments.getArgument("GetCount")) +" ;");

            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));

            rs = st.executeQuery();
            int rowSize = 0;
			int colSize = rs.getMetaData().getColumnCount();
			JsonObject e = new JsonObject();
			while(rs.next())
			{
			    JsonObject m = new JsonObject();
			    for (int i = 1; i < colSize+1; i++)
                {
                    m.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                e.addProperty(java.lang.String.valueOf(rowSize++), m.toString());
			}

            logger.debug("Selected divisions for tourn: " + (String)arguments.getArgument("TournamentID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting divisions for tourn: " + (String)arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting divisions for tourn: " + (String)arguments.getArgument("TournamentID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}


	public String getDivisionDataForPlayer(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("select d.*, p.id_player1 as pid1, p.id_player2 as pid2 from division d left join (select * from player where id_division = ? and (id_player1 = ? or id_player2 = ?)) as p on p.id_division = d.id where d.id = ?;");

            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.setInt(4, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));

            rs = st.executeQuery();
	        rs.next();
	        int size = rs.getMetaData().getColumnCount();

            JsonObject e = new JsonObject();

	        for (int i = 1; i < size+1; i++)
	        {
	            if (!(rs.getMetaData().getColumnName(i).equalsIgnoreCase("password")))
                    e.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
            }

            logger.debug("Selected divisions data for player: " + (String)arguments.getArgument("PersonID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting divisions data for player: " + (String)arguments.getArgument("PersonID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting divisions data for player: " + (String)arguments.getArgument("PersonID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	//Get table count by name
	public String getDivisionCountForPlayer(CommandArguments arguments)
	{
	    try {
	        Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("select count(*) from player where division_id in (select id from divison where id_tournament = ?) and (id_player1 = ? or id_player2 = ?);");
	        st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("TournamentID")));
            st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));
            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("PersonID")));

	        rs = st.executeQuery();
	        rs.next();
	        int count = Integer.parseInt(rs.getString(1));

			JsonObject e = new JsonObject();
            e.addProperty("result", count);
            logger.debug("Selected count on divisions for player: " + (String)arguments.getArgument("PersonID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting count on divisions for player: " + (String)arguments.getArgument("PersonID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");

            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting count on divisions for player: " + (String)arguments.getArgument("PersonID"));
	        logger.error("error: " + ex);
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
            st = conn.prepareStatement("SELECT `isDouble` FROM `division` WHERE `id` = ?;");
            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
	        rs = st.executeQuery();
	        rs.next();
	        int isDouble = Integer.parseInt(rs.getString(1));
	        logger.trace("isDouble? " + isDouble);

            st = conn.prepareStatement ("INSERT INTO `player` (`id_player1`, `id_player2`, `id_division`) VALUES (?, ?, ?);");

            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("Person1ID")));

            if (isDouble == 1)
                st.setInt(2, java.lang.Integer.valueOf((String)arguments.getArgument("Persor2ID")));
            else
                st.setNull(2, java.sql.Types.INTEGER);

            st.setInt(3, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));

            st.executeUpdate();

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

            conn.close();

	        logger.debug("Player added (p1)pid: " + arguments.getArgument("Player1ID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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
	        logger.error("error: " + ex);
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

            logger.debug("Selected pid from player with player1 pid: " + arguments.getArgument("Player1ID"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting plid: " + arguments.getArgument("Player1ID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting plid: " + arguments.getArgument("Player1ID"));
	        logger.error("error: " + ex);
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

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Match created matchno: " + arguments.getArgument("MatchNumber"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

	public String getMatchInfo(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);
            st = conn.prepareStatement ("SELECT m.*, p1p1.name as p1p1, p1p2.name as p1p2, p2p1.name as p2p1, p2p2.name as p2p2, p1p1.id as p1p1_id, p1p2.id as p1p2_id, p2p1.id as p2p1_id, p2p2.id as p2p2_id FROM `match` m left join player pl1 on m.id_player1 = pl1.id left join person p1p1 on pl1.id_player1 = p1p1.id left join person p1p2 on pl1.id_player2 = p1p2.id left join player pl2 on m.id_player2 = pl2.id left join person p2p1 on pl2.id_player1 = p2p1.id left join person p2p2 on pl2.id_player2 = p2p2.id where m.id_division = ? ORDER BY m.matchNumber;");

            st.setInt(1, java.lang.Integer.valueOf((String)arguments.getArgument("DivisionID")));
	        rs = st.executeQuery();
	        int colSize = rs.getMetaData().getColumnCount();
            int rowSize = 1;
			JsonObject e = new JsonObject();
			while(rs.next())
			{
			    JsonObject m = new JsonObject();
			    for (int i = 1; i < colSize+1; i++)
                        m.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
                e.addProperty(java.lang.String.valueOf(rowSize++), m.toString());
			}

            logger.trace("Selected match data for match id: " + arguments.getArgument("DivisionID"));
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting data from match with id: " + arguments.getArgument("DivisionID"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting data from match with id: " + arguments.getArgument("DivisionID"));
	        logger.error("error: " + ex);
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

            logger.debug("Selected mid from match num: " + arguments.getArgument("MatchNumber"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting mid: " + arguments.getArgument("MatchNumber"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting mid: " + arguments.getArgument("MatchNumber"));
	        logger.error("error: " + ex);
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

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Game created for matchID: " + arguments.getArgument("MatchID"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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

            logger.debug("Selected gid from game: " + arguments.getArgument("GameNumber"));

            JsonObject e = new JsonObject();
            e.addProperty("result", id);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting gid: " + arguments.getArgument("GameNumber"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting gid: " + arguments.getArgument("GameNumber"));
	        logger.error("error: " + ex);
			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
        }
	}

	public String updateGameScore(CommandArguments arguments)
	{
	try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            int indexOfID = 1;
            boolean p1 = false;
            boolean p2 = false;

            String sql = "UPDATE `game` ";

	        if (arguments.doesKeyExist("Player1Score"))
	        {
                p1 = true;
                sql += " SET `team1Score` = ? ";
            }

            if (arguments.doesKeyExist("Player1Score"))
	        {
                if (p1) sql += ", ";
                p2 = true;
                sql += " SET `team2Score` = ? ";
            }


            st = conn.prepareStatement ("UPDATE `game` SET `team1Score` = ?, SET `team2Score` = ? WHERE `id` = ?;");


            st.setDate(2, java.sql.Date.valueOf((String)arguments.getArgument("Player1Score")));


            st.setInt(indexOfID, java.lang.Integer.valueOf((String)arguments.getArgument("GameID")));

            st.executeUpdate();


	        long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Person: " + arguments.getArgument("PersonID") + " unavailTimes updated");

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
            return e.toString();
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception during updatePersonAvail:\n" + ex);
	        JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e.toString();
	    }
        catch (Exception ex)
        {
	        logger.error("Java Exception during updatePersonAvail:\n" + ex);

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

            long key = -1;
	        ResultSet genKey = st.getGeneratedKeys();
	        if (genKey.next())
	            key = genKey.getLong(1);

	        conn.close();

    		logger.debug("Foul Created: " + arguments.getArgument("FoulName"));

	        JsonObject e = new JsonObject();
            e.addProperty("result", key);
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
