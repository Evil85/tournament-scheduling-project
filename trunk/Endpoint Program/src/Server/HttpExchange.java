package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Random;

/**
 * User: Chris
 * <p/>
 * Date: 10/8/11
 * <p/>
 * HttpExchange is used to
 */
public class HttpExchange extends EventObject
{
	private static final Random RandomIdGenerator = new Random();

	private Socket connectionSocket;

	private Date exchangeTime;

	private int exchangeId;

	public HttpExchange(Socket connectionSocket)
	{
		super(connectionSocket);
		this.connectionSocket = connectionSocket;
		this.exchangeTime = new Date();
		this.exchangeId = RandomIdGenerator.nextInt(1000);
	}

	public String getExchangeTime()
	{
		return this.exchangeTime.toString();
	}

	public int getExchangeId()
	{
		return this.exchangeId;
	}

	public String getRequest()
	{
		return this.getMessageFromClient();
	}

	public void sendResponse(String message)
	{
		try
		{
			BufferedOutputStream outputStream = new BufferedOutputStream(connectionSocket.getOutputStream());
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
			outputStreamWriter.write(message);
			outputStreamWriter.flush();
		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}

	private String getMessageFromClient()
	{
		StringBuilder stringFromClient = new StringBuilder();
		try
		{
			BufferedInputStream inputStreamFromClient = new BufferedInputStream(connectionSocket.getInputStream());
			InputStreamReader streamReaderFromClient = new InputStreamReader(inputStreamFromClient);

			int character;

			// 13 is the ASCII character for carriage return or /r
			// Read one character at a time from the socket and append it to the string buffer.
			while ((character = streamReaderFromClient.read()) != 13)
			{
				stringFromClient.append((char) character);
			}
		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}

		return stringFromClient.toString();
	}

	@Override public String toString()
	{
		Date currentTime = new Date();
		double connectionOpenTime = (currentTime.getTime() - this.exchangeTime.getTime()) * .001;
		DecimalFormat format = new DecimalFormat("###.###");
		return "Connection Address:      " + this.connectionSocket.getInetAddress() + ":" + this.connectionSocket.getLocalPort() + "\n" +
		       "Initial Connection Time: " + this.getExchangeTime() + "\n" +
		       "Current Server Time:     " + currentTime.toString() + "\n" +
		       "Connection open for:     " + format.format(connectionOpenTime) + " seconds\n" +
		       "Exchange ID:             " + this.getExchangeId();
	}
}
