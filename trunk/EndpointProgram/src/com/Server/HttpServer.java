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

	private boolean isServerRunning;

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
		this.isServerRunning = true;

		try
		{
			ServerSocket socket = new ServerSocket(port);
			logger.info("Server Initialized on port: " + port);

			while (this.isServerRunning)
			{
				// otherwise keep the socket open until the user says to close it.
				final Socket newConnection = socket.accept();

				// Spawn a new thread for the new connectionSocket to run on.
				// This will allow us to have multiple connections.
				this.threadExecutor.execute(new Runnable()
				{
					public void run()
					{
						numberOfConnections++;
						HttpExchange exchange = new HttpExchange(newConnection);
						logger.debug("Connection Established - ID : " + exchange.getExchangeId());
						logger.trace("Connection Info:\n" + exchange.toString());
						requestHandler.onConnect(exchange);

						if (exchange.getMessageFromClient())
						{
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

						logger.debug("Connection Closed - ID : " + exchange.getExchangeId());
						logger.trace("Connection Info:\n" + exchange.toString());
						requestHandler.onDisconnect(exchange);
						numberOfConnections--;
					}
				});
			}
			logger.info("Server Shutdown");
		}
		catch (IOException e)
		{
			logger.error(e);
		}
	}

	public void stop()
	{
		this.isServerRunning = false;
	}
}
