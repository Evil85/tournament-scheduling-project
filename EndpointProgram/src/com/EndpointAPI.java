package com;

import org.apache.log4j.Logger;

import com.Utilities.CommandArguments;

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
		logger.debug(arguments.getArgument("Username"));
		logger.info("User Created");
	}
}
