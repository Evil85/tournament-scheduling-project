package com.Server;

import java.util.EventListener;

/**
 * User: Chris
 * <p/>
 * Date: 10/8/11
 * <p/>
 * IHttpHandler is used to
 */
public interface IHttpHandler extends EventListener
{
	public void onConnect(HttpExchange clientExchange);

	public void onRequest(HttpExchange clientExchange);

	public void onDisconnect(HttpExchange clientExchange);
}
