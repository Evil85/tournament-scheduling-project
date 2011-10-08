package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * User: Chris
 * <p/>
 * Date: 10/8/11
 * <p/>
 * HttpServer is used to
 */
public class HttpServer
{
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
			System.out.println("Server Initialized on port: " + port);

			// TODO: Make a way to end this while loop to shut down the server safely.
			while (true)
			{
				System.out.println("Waiting for connection...");

				final Socket newConnection = socket.accept();

				// Spawn a new thread for the new connectionSocket to run on.
				// This will allow us to have multiple connections.
				this.threadExecutor.execute(new Runnable()
				{
					public void run()
					{
						numberOfConnections++;
						HttpExchange exchange = new HttpExchange(newConnection);
						System.out.println("Connection Established: \n" + exchange.toString() + "\n");
						requestHandler.onConnect(exchange);
						requestHandler.onRequest(exchange);

						// Close the connectionSocket once we are done with it
						try
						{
							newConnection.close();
						}
						catch (IOException e)
						{
							System.out.println(e);
						}

						System.out.println("Connection Closed: \n" + exchange.toString() + "\n");
						requestHandler.onDisconnect(exchange);
						numberOfConnections--;
					}
				});
			}
			// System.out.println("Server Shutdown");
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
}
