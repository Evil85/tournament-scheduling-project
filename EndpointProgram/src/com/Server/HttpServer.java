package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * User: Chris
 * <p/>
 * Date: 10/8/11
 * <p/>
 * HttpServer is used to
 */
public class HttpServer
{
	private static final Logger logger = Logger.getLogger(HttpServer.class);

	// Used for managing multiple threads that the connections will be processed on.
	private Executor threadExecutor;

	private int port;

	private int numberOfConnections;

	private IHttpHandler requestHandler;

	public int getNumberOfClientConnections()
	{
		return this.numberOfConnections;
	}

	public HttpServer(int port, IHttpHandler requestHandler)
	{
		this.port = port;
		this.threadExecutor = Executors.newCachedThreadPool();
		this.requestHandler = requestHandler;
	}

	/**
	 * This will start the server and wait for a connectionSocket on the given port.
	 * This is where the socket will wait until it has found a connectionSocket and then it will process the connectionSocket on a new thread.
	 */
	public void run()
	{
		try
		{
			ServerSocket socket = new ServerSocket(port);
			logger.info("Server Initialized on port: " + port);

			// TODO: Make a way to end this while loop to shut down the server safely.
			while (true)
			{
				//logger.info("Waiting for connection...");

				// TODO: If it is a localhost connection the we can just close the connection after we have recieved the message
				// otherwise keep the socket open untill the user says to close it.
				final Socket newConnection = socket.accept();

				// Spawn a new thread for the new connectionSocket to run on.
				// This will allow us to have multiple connections.
				this.threadExecutor.execute(new Runnable()
				{
					public void run()
					{
						numberOfConnections++;
						HttpExchange exchange = new HttpExchange(newConnection);
						logger.info("Connection Established - ID : " + exchange.getExchangeId());
						logger.debug("Connection Info:\n" + exchange.toString());
						requestHandler.onConnect(exchange);

						// TODO: Make exchange handle multipule requests.
						// TODO: Call get message here
						while (exchange.getMessageFromClient())
						{
							//exchange.getMessageFromClient();
							logger.info("Request Received - ID : " + exchange.getExchangeId());
							logger.debug("Request Message - ID : " + exchange.getExchangeId() + "\n" + exchange.getRequest());
							requestHandler.onRequest(exchange);
						}

						// Close the connectionSocket once we are done with it
						try
						{
							newConnection.close();
						}
						catch (IOException e)
						{
							logger.error(e);
						}

						logger.info("Connection Closed - ID : " + exchange.getExchangeId());
						logger.debug("Connection Info:\n" + exchange.toString());
						requestHandler.onDisconnect(exchange);
						numberOfConnections--;
					}
				});
			}
			// System.out.println("Server Shutdown");
		}
		catch (IOException e)
		{
			logger.error(e);
		}
	}

	public void stop()
	{
	// TODO: Fill this in
	}
}
