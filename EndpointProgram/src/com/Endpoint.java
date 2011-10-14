package com;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.Server.HttpExchange;
import com.Server.HttpServer;
import com.Server.IHttpHandler;
import com.Utilities.CommandArguments;

/**
 * User: Chris
 * <p/>
 * Date: 10/5/11
 * <p/>
 * Endpoint is as a main class to interact with a client through the specified arguments.
 */
public class Endpoint
{
	private static final Logger logger = Logger.getLogger(Endpoint.class);

	/**
	 * This is the main entry point of the program.
	 * @param args args[0] will always be a json string that will be passed in by the client from the website.
	 */
	public static void main(String[] args)
	{
		IHttpHandler handler = new IHttpHandler()
		{
			public void onConnect(HttpExchange clientExchange)
			{
			}

			public void onRequest(HttpExchange clientExchange)
			{
				processJson(clientExchange.getRequest());
				String timeStamp = new Date().toString();
				String returnMessage = "Server responded at " + timeStamp + (char) 13;
				clientExchange.sendResponse(returnMessage);
			}

			public void onDisconnect(HttpExchange clientExchange)
			{
			}
		};

		HttpServer endPointServer = new HttpServer(8000, handler);
		endPointServer.run();
	}

	/**
	 * This will handle the logic for parsing the json string received from the client. It will then call the respective command in the EndpointAPI.
	 *
	 * @param jsonString the string received from the client.
	 */
	private static void processJson(String jsonString)
	{
		if (jsonString == null ||
		    jsonString.isEmpty())
		{
			logger.error("There is nothing in the json string.");
			return;
		}

		// Creates a wrapper for the arguments. This can be used to get arguments by a key value.
		CommandArguments commandArguments;
		try
		{
			commandArguments = new CommandArguments(jsonString);
		}
		catch (Exception ex)
		{
			System.err.println("Could not deserialize json object : " + jsonString);
			return;
		}

		EndpointAPI endpointAPI = new EndpointAPI();


		// Use reflection to call the correct command.
		try
		{
			logger.info("Calling command: " + commandArguments.getCommandName());
			logger.debug("Command Parameters: " + commandArguments.toString());
			Class.forName("com.EndpointAPI").getMethod(commandArguments.getCommandName(), CommandArguments.class).invoke(endpointAPI, commandArguments);
		}
		catch (IllegalAccessException e)
		{
			logger.error(e);
		}
		catch (InvocationTargetException e)
		{
			logger.error(e);
		}
		catch (NoSuchMethodException e)
		{
			System.err.println("The given command is not in the Endpoint API.");
		}
		catch (ClassNotFoundException e)
		{
			logger.error(e);
		}
	}
}
