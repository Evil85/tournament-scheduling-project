package com.Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * User: Chris
 * <p/>
 * Date: 10/8/11
 * <p/>
 * HttpExchange is used to
 */
public class HttpExchange extends EventObject
{
	private static final Logger logger = Logger.getLogger(HttpExchange.class);

	private static final Random RandomIdGenerator = new Random();

	private Socket connectionSocket;

	private Date exchangeTime;

	private int exchangeId;

	private static int totalExchangeCount;

	private String clientRequest;

	// 13 is the ASCII character for carriage return or /r
	private final int requestDelimiter = 13;

	public HttpExchange(Socket connectionSocket)
	{
		super(connectionSocket);
		this.connectionSocket = connectionSocket;
		this.exchangeTime = new Date();
		totalExchangeCount++;
		this.exchangeId = RandomIdGenerator.nextInt(1000);
	}

	public String getExchangeTime()
	{
		return this.exchangeTime.toString();
	}

	public int getTotalExchangeCount()
	{
		return totalExchangeCount;
	}

	public int getExchangeId()
	{
		return this.exchangeId;
	}

	public String getRequest()
	{
		return this.clientRequest;
	}

	public void sendResponse(String message)
	{
		try
		{
			logger.trace("About to respond to the client");
			// TODO: Persist this write, ex if the client disconnects then we need to keep the message to send around until it reconnects.
			BufferedOutputStream outputStream = new BufferedOutputStream(connectionSocket.getOutputStream());
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
			outputStreamWriter.write(message);
			outputStreamWriter.flush();
			logger.trace("Response Sent - ID : " + this.getExchangeId());
			logger.trace("Response Message - ID : " + this.getExchangeId() + " :\n" + message);
		}
		catch (IOException ex)
		{
			logger.error(ex);
		}
		catch (Exception ex)
		{
			logger.error(ex);
		}
	}

	boolean getMessageFromClient()
	{
		StringBuilder stringFromClient = new StringBuilder();
		try
		{
			BufferedReader inputStreamFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			int character;

			logger.trace("About to read request from client");

			// 13 is the ASCII character for carriage return or /r
			// Read one character at a time from the socket and append it to the string buffer.
			while ((character = inputStreamFromClient.read()) != requestDelimiter &&
			         character != -1)
			{
				stringFromClient.append((char) character);
			}

			if (stringFromClient.length() == 0)
			{
				logger.trace("The request was empty");
				return false;
			}

			logger.trace("Finished reading request from client");
		}
		catch (IOException ex)
		{
			logger.trace("Ignoring: " + ex);
			return false;
		}
		catch (Exception ex)
		{
			logger.error(ex);
			return false;
		}

		this.clientRequest = stringFromClient.toString();
		logger.trace("Request Received - ID : " + this.getExchangeId());
		logger.trace("Request Message - ID : " + this.getExchangeId() + " :\n" + this.getRequest());

		return true;
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
