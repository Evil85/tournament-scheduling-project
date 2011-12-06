package com;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Level;
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
	 * @param args arguments passed in by the console.
	 */
	public static void main(String[] args)
	{
		int port = 4545;

		for (int i = 0; i < args.length; i++)
		{
			// Enable Verbose mode.
			if (args[i].equals("-v"))
			{
				Logger.getRootLogger().setLevel(Level.ALL);
                Logger.getLogger(EndpointAPI.class).setLevel(Level.ALL);
				logger.info("Verbose mode enabled");
			}

			// Handle port change
			if (args[i].equals("-p"))
			{
				port = Integer.parseInt(args[i + 1]);
				i++;
				logger.info("Port set to " + port);
			}
		}

		IHttpHandler handler = new IHttpHandler()
		{
			public void onConnect(HttpExchange clientExchange)
			{
			}

			public void onRequest(HttpExchange clientExchange)
			{
				String result = processRequest(clientExchange.getRequest());

				if (result != null)
				{
					clientExchange.sendResponse(result);
				}
				else
				{
					logger.error("Resulting JsonObject is null");
				}

				// TODO: Parse shutdown/restart server.
			}

			public void onDisconnect(HttpExchange clientExchange)
			{
			}
		};

		HttpServer endPointServer = new HttpServer(port, handler);
		endPointServer.run();
	}

	/**
	 * This will handle the logic for parsing the json string received from the client. It will then call the respective command in the EndpointAPI.
	 *
	 * @param jsonString the string received from the client.
	 * @return The response object from the processed json object.
	 */
	private static String processRequest(String jsonString)
	{
		if (jsonString == null ||
		    jsonString.isEmpty())
		{
			logger.error("There is nothing in the json string.");
			return null;
		}

		// Creates a wrapper for the arguments. This can be used to get arguments by a key value.
		CommandArguments commandArguments;
		try
		{
			commandArguments = new CommandArguments(jsonString);
		}
		catch (Exception ex)
		{
			logger.error("Could not deserialize json object : " + jsonString);
			return null;
		}

		EndpointAPI endpointAPI = new EndpointAPI();


		// Use reflection to call the correct command.
		try
		{
			logger.info("Calling command: " + commandArguments.getCommandName());
			logger.debug("Command Parameters: " + commandArguments.toString());
			return (String)Class.forName("com.EndpointAPI").getMethod(commandArguments.getCommandName(), CommandArguments.class).invoke(endpointAPI, commandArguments);
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
			logger.error("The given command is not in the Endpoint API.");
		}
		catch (ClassNotFoundException e)
		{
			logger.error(e);
		}

		return null;
	}
}
