/*
 * Dog - WebSocket Environment Endpoint
 * 
 * Copyright (c) 2014 Luigi De Russis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package it.polito.elite.dog.communication.websocket.environment.api;

import it.polito.elite.dog.communication.rest.environment.api.EnvironmentRESTApi;
import it.polito.elite.dog.communication.websocket.api.WebSocketConnector;
import it.polito.elite.dog.core.library.util.LogHelper;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 * Represent the WebSocket endpoint for handling environment-related
 * information.
 * 
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @see <a href="http://elite.polito.it">http://elite.polito.it</a>
 * 
 */
public class EnvironmentWebSocketEndpoint
{
	// reference for the EnvironmentRESTApi
	private AtomicReference<EnvironmentRESTApi> restApi;
	// reference for the WebSocket connector
	private AtomicReference<WebSocketConnector> websocketConnector;
	
	// the bundle context reference
	private BundleContext context;
	
	// the service logger
	private LogHelper logger;
	
	/**
	 * Default constructor
	 */
	public EnvironmentWebSocketEndpoint()
	{
		// init
		this.restApi = new AtomicReference<EnvironmentRESTApi>();
		this.websocketConnector = new AtomicReference<WebSocketConnector>();
	}
	
	/**
	 * Bundle activation, stores a reference to the context object passed by the
	 * framework to get access to system data, e.g., installed bundles, etc.
	 * 
	 * @param context
	 *            the OSGi bundle context
	 */
	public void activate(BundleContext context)
	{
		// store the bundle context
		this.context = context;
		
		// init the logger
		this.logger = new LogHelper(this.context);
		
		// log the activation
		this.logger.log(LogService.LOG_INFO, "Activated....");
		
		this.websocketConnector.get().registerEndpoint(this, this.restApi.get());
	}
	
	/**
	 * Bundle stop
	 */
	public void deactivate()
	{
		// nullify everything...
		this.context = null;
		this.logger = null;
	}
	
	/**
	 * Bind the EnvironmentRESTApi service (before the bundle activation)
	 * 
	 * @param environmentRestApi
	 *            the EnvironmentRestApi service to add
	 */
	public void addedRESTApi(EnvironmentRESTApi environmentRestApi)
	{
		// store a reference to the EnvironmentRESTApi service
		this.restApi.set(environmentRestApi);
	}
	
	/**
	 * Unbind the EnvironmentRESTApi service
	 * 
	 * @param environmentRestApi
	 *            the EnvironmentRESTApi service to remove
	 */
	public void removedRESTApi(EnvironmentRESTApi environmentRestApi)
	{
		this.restApi.compareAndSet(environmentRestApi, null);
	}
	
	/**
	 * Bind the WebSocketConnector service (before the bundle activation)
	 * 
	 * @param websocket
	 *            the websocket connector service to add
	 */
	public void addedWebSocketConnector(WebSocketConnector websocket)
	{
		// store a reference to the WebSocketConnector service
		this.websocketConnector.set(websocket);
	}
	
	/**
	 * Unbind the WebSocketConnector service
	 * 
	 * @param websocket
	 *            the websocket connector service to remove
	 */
	public void removedWebSocketConnector(WebSocketConnector websocket)
	{
		this.websocketConnector.compareAndSet(websocket, null);
	}
}
