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

import sun.rmi.runtime.Log;

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
		// If the client has closed the connection then stop listening.
		if (!this.connectionSocket.isConnected() ||
			this.connectionSocket.isOutputShutdown())
		{
			logger.debug("Trying to send response but connection is closed.");
			return;
		}

		try
		{
			// TODO: Persist this write, ex if the client disconnects then we need to keep the message to send around until it reconnects.
			BufferedOutputStream outputStream = new BufferedOutputStream(connectionSocket.getOutputStream());
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
			outputStreamWriter.write(message);
			outputStreamWriter.flush();
			logger.info("Response Sent - ID : " + this.getExchangeId());
			logger.debug("Response Message - ID : " + this.getExchangeId() + " -\n" + message);
		}
		catch (IOException ex)
		{
			logger.error(ex);
		}
	}

	boolean getMessageFromClient()
	{
		// If the client has closed the connection then stop listening.
		if (!this.connectionSocket.isConnected() ||
		    connectionSocket.isInputShutdown())
		{
			logger.debug("Trying to receive a response but connection is closed.");
			return false;
		}

		StringBuilder stringFromClient = new StringBuilder();
		try
		{
			BufferedReader inputStreamFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

			int character;

			logger.trace("About to read from input stream");

			// 13 is the ASCII character for carriage return or /r
			// Read one character at a time from the socket and append it to the string buffer.
			// TODO: Change to constant delimiter
			while ((character = inputStreamFromClient.read()) != 13)
			{
				if (connectionSocket.isInputShutdown())
				{
					Log
				}
				stringFromClient.append((char) character);
				logger.trace("Read char: " + (char)character + " (" + character + ")");

				// This means we have reached the end of the stream but the user has not specified the correct delimiter.
				if (character == -1)
				{
					logger.debug("User did not specific the correct delimiter and we have reached the end of the stream.");
					return false;
				}
			}

			logger.trace("Finished reading from input stream.");
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
		logger.info("Request Received - ID : " + this.getExchangeId());
		logger.debug("Request Message - ID : " + this.getExchangeId() + " -\n" + this.getRequest());

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
