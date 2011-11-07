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

	public JsonObject createUser(CommandArguments arguments)
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

	
	public JsonObject getPersonID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `pid` FROM `person` WHERE `name` = ?;");
            st.setString(1, (String)arguments.getArgument("PersonName"));
	        ResultSet rs = st.executeQuery();
	        rs.next();
	        int pid = Integer.parseInt(rs.getString(1));

            logger.info("Selected pid from Person: " + arguments.getArgument("PersonName"));

            JsonObject e = new JsonObject();
            e.addProperty("pid", pid);
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting pid: " + arguments.getArgument("PersonName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting pid: " + arguments.getArgument("PersonName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
	}
	
	public JsonObject getUserID(CommandArguments arguments)
	{
	    try {
            Connection conn = DriverManager.getConnection(URL, user, pass);

            st = conn.prepareStatement("SELECT `uid` FROM `user` WHERE `username` = ?;");
            st.setString(1, (String)arguments.getArgument("UserName"));
	        ResultSet rs = st.executeQuery();
	        rs.next();
	        int uid = Integer.parseInt(rs.getString(1));

            logger.info("Selected uid from user: " + arguments.getArgument("UserName"));

            JsonObject e = new JsonObject();
            e.addProperty("uid", uid);
            return e;
        }
        catch (SQLException ex)
        {
	        logger.error("SQL Exception while selecting uid: " + arguments.getArgument("UserName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
        catch (Exception ex)
        {
	        logger.error("Java Exception while selecting uid: " + arguments.getArgument("UserName"));

			JsonObject e = new JsonObject();
            e.addProperty("result", "false");
            return e;
        }
	}
}
