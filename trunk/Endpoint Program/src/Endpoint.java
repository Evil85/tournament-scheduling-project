import java.lang.reflect.InvocationTargetException;

import Utilities.CommandArguments;

/**
 * User: Chris
 * <p/>
 * Date: 10/5/11
 * <p/>
 * Endpoint is as a main class to interact with a client through the specified arguments.
 */
public class Endpoint
{
	/**
	 * This is the main entry point of the program.
	 * @param args args[0] will always be a json string that will be passed in by the client from the website.
	 */
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("There are not enough arguments. Need at least one.");
			return;
		}

		// Creates a wrapper for the arguments. This can be used to get arguments by a key value.
		CommandArguments commandArguments = new CommandArguments(args[0]);
		EndpointAPI endpointAPI = new EndpointAPI();

		// Use reflection to call the correct command.
		try
		{
			Class.forName("EndpointAPI").getMethod(commandArguments.getCommandName(), CommandArguments.class).invoke(endpointAPI, commandArguments);
		}
		catch (IllegalAccessException e)
		{
			System.out.println(e);
		}
		catch (InvocationTargetException e)
		{
			System.out.println(e);
		}
		catch (NoSuchMethodException e)
		{
			System.out.println(e);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
	}
}
