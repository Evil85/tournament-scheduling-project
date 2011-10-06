import Utilities.CommandArguments;

/**
 * User: Chris
 * <p/>
 * Date: 10/5/11
 * <p/>
 * EndpointAPI is used to
 */
public class EndpointAPI
{
	public void createUser(CommandArguments arguments)
	{
		System.out.println(arguments.getArgument("Username"));
		System.out.println("User Created");
	}
}
