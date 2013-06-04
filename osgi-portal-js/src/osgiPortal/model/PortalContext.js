'use strict';

window.OsgiPortal = window.OsgiPortal || {};

(function(OsgiPortal, undefined) {

	var Tools = MbyUtils.Tools;

	var EventTarget = MbyUtils.event.EventTarget;
	var EventListener = MbyUtils.event.EventListener;

	var Reply = OsgiPortal.model.Reply;

	/**
	 * PortalContext builder. The OSGi Portal context.
	 */
	var PortalContext = function(configuration) {

		if (!configuration) {
			throw "No configuration supplied to PortalContext !";
		}

		/** Event Hooks configuration. Map(topic => EventHook) */
		this.portalEventHooks = {};

		/** Action Hooks configuration. Map(type => ActionHook) */
		this.portalActionHooks = {};

		/** Registered Apps Map(appId => App). */
		this.registeredApps = {};

		/** Registered AppClients Map(appId => App). */
		this.registeredAppClients = {};

		/** List of each App EventTarget wich allow the App to fire Events. Map(app symbolic name => EventTarget) */
		this.appEventTargets = {};

		var eventHooks = configuration.eventHooks;
		if (!eventHooks) {
			eventHooks = {};
		}
		this.portalEventHooks = eventHooks;

		var actionHooks = configuration.actionHooks;
		if (!actionHooks) {
			actionHooks = {};
		}
		this.portalActionHooks = actionHooks;
	};

	/** Retrieve an App by its Id. */
	PortalContext.prototype.getRegisteredAppById = function(appId) {
		var app = this.registeredApps[appId];

		if (!app) {
			return null;
		}

		return app;
	};

	/** Retrieve an App by its Id. */
	PortalContext.prototype.getRegisteredAppBySymbolicName = function(symbolicName) {
		for ( var appId in this.registeredApps) {
			if (this.registeredApps.hasOwnProperty(appId)) {
				var app = this.registeredApps[appId];
				if (app && app.symbolicName === symbolicName) {
					return app;
				}
			}
		}

		return null;
	};

	/** Retrieve an AppClient by its Id. */
	PortalContext.prototype.getRegisteredAppClientById = function(appId) {
		var appClient = this.registeredAppClients[appId];

		if (!appClient) {
			return null;
		}

		return appClient;
	};

	/** Retrieve an App EventTarget by symbolicName. */
	PortalContext.prototype.getAppEventTarget = function(symbolicName) {
		var eventTarget = this.appEventTargets[symbolicName];

		if (!eventTarget) {
			eventTarget = new EventTarget("App sn:[" + symbolicName + "]");
			this.appEventTargets[symbolicName] = eventTarget;
		}

		return eventTarget;
	};

	/** Add an App and its Client in the context. */
	PortalContext.prototype.registerApp = function(app, appClient) {
		// Register App
		if (!app) {
			throw "No App supplied for registration in PortalContext";
		}

		if (app.id !== appClient.appId) {
			throw "App and its client does not share the same appId !";
		}

		var registeredApp = this.registeredApps[app.id];
		if (registeredApp) {
			throw app + " already registered !";
		}
		this.registeredApps[app.id] = app;
		console.log(app + " registered in PortalContext.");

		var appEventTarget = this.getAppEventTarget(app.symbolicName);

		// Register Portal EventHooks on App
		for ( var eventTopic in this.portalEventHooks) {
			if (this.portalEventHooks.hasOwnProperty(eventTopic)) {
				var eventHook = this.portalEventHooks[eventTopic];
				if (eventHook && Tools.isFunction(eventHook)) {
					var hookListener = new EventListener(eventTopic, eventHook);
					appEventTarget.addEventListener(hookListener);
					console.log("Portal EventHook#" + eventTopic + " registered on " + app + ".");
				} else {
					console.log("Portal EventHook#" + eventTopic + " is not defined !");
				}
			}
		}

		// Register AppClient
		if (!appClient) {
			throw "No AppClient supplied for registration in PortalContext";
		}
		var registeredAppClient = this.registeredAppClients[appClient.appId];
		if (registeredAppClient) {
			throw appClient + " already registered !";
		}
		this.registeredAppClients[appClient.appId] = appClient;
		console.log(appClient + " registered in PortalContext.");

		// Event Wiring : add all listeners for this App
		for ( var k = 0; k < app.eventWiring.length; ++k) {
			var wire = app.eventWiring[k];

			// Listener info
			var topic = wire.topic;
			var symbolicName = wire.symbolicName;

			if (!topic || !symbolicName) {
				throw "Event wiring need to contain a property 'symbolicName' and a property 'topic' !";
			}

			var appToListenEventTarget = this.getAppEventTarget(symbolicName);
			// Build a listener with a null callback for this wire.
			var wireListener = new EventListener(topic, null);
			appToListenEventTarget.addEventListener(wireListener);

			// Add listener to AppClient
			var clientListeners = appClient.appEventListeners;
			if (!clientListeners) {
				clientListeners = [];
				appClient.appEventListeners = clientListeners;
			}
			if (!clientListeners[symbolicName]) {
				clientListeners[symbolicName] = [];
				appClient.appEventListeners[symbolicName] = clientListeners[symbolicName];
			}

			appClient.appEventListeners[symbolicName][topic] = wireListener;

			console.log(app + " wiring configured to listen App with sn: [" + symbolicName + "] on topic: [" + topic
					+ "].");
		}

	};

	/** Remove an App from context. */
	PortalContext.prototype.unregisterApp = function(app) {
		var appId = app.id;
		var removedApp = this.getRegisteredAppClientById(appId);
		if (removedApp) {
			this.registeredApps[appId] = null;
			console.log(removedApp + " unregistered.");
		} else {
			throw app + " wasn't previously registered !";
		}

		var removedClient = this.getRegisteredAppClientById(appId);
		if (removedClient) {
			this.registeredAppClients[appId] = null;
			console.log(removedClient + " unregistered.");
		} else {
			throw appClient + " wasn't previously registered !";
		}
	};

	/** Remove an App from context by Id. */
	PortalContext.prototype.unregisterAppById = function(appId) {
		var app = this.getRegisteredAppById(appId);
		this.unregisterApp(app);
	};

	/** Fire an event from an AppClient. */
	PortalContext.prototype.fireEventFromAppClient = function(appClient, event) {
		var portalContext = this;

		// Check the validity of the client
		checkAppClientValidity(portalContext, appClient);

		var app = this.getRegisteredAppById(appClient.appId);
		var appSn = app.symbolicName;

		event.properties.sourceSymbolicName = appSn;
		event.properties.sourceVersion = app.version;

		var appEventTarget = this.getAppEventTarget(appSn);
		appEventTarget.fireEvent(event);
	};

	/** Do an Action on the Portal form an AppClient. */
	PortalContext.prototype.doActionFromAppClient = function(appClient, action) {
		var portalContext = this;

		// Check the validity of the client
		checkAppClientValidity(portalContext, appClient);

		console.log(appClient + " doing " + action + " ...");

		var actionType = action.type;
		var actionHook = this.portalActionHooks[actionType];
		var app = this.getRegisteredAppById(appClient.appId);

		action.properties.sourceSymbolicName = app.symbolicName;
		action.properties.sourceVersion = app.version;

		if (actionHook) {
			if (!Tools.isFunction(actionHook)) {
				throw "Configured Portal Action hook for type " + actionType + " is not a function !";
			}

			// By default appId is the source of the Action
			var defaultAppId = appClient.appId;

			// Call Action Hook passing the action and the App
			var replyCallback = function(replyType, properties, appId) {
				appId = appId || defaultAppId;

				sendReplyToAppClient(portalContext, appId, new Reply(replyType, properties));
			};

			actionHook(action, replyCallback);
		}

	};

	/** Call the Hook associated to the Reply on the AppClient. */
	function sendReplyToAppClient(portalContext, appId, reply) {
		var appClient = portalContext.getRegisteredAppClientById(appId);
		if (!appClient) {
			throw "No AppClient found for appId#" + appId + " to send a reply to !";
		}

		var replyType = reply.type;
		var clientReplyHook = appClient.replyHooks[replyType];

		if (clientReplyHook) {
			if (!Tools.isFunction(clientReplyHook)) {
				throw "Configured " + appClient + " Reply hook for type " + replyType + " is not a function !";
			}

			console.log("Calling Reply hook for type " + replyType + " on " + appClient + " ...");
			// Call client reply hook
			clientReplyHook(reply);
		} else {
			console.log("No Hook registered for incoming " + reply + " to " + appClient + ".");
		}
	};

	/**
	 * Check the validity of an AppClient (do we built it ?).
	 */
	function checkAppClientValidity(portalContext, appClient) {
		if (!appClient) {
			throw "AppClient is null or undefined !";
		}

		var appId = appClient.appId;
		var registeredAppClient = portalContext.getRegisteredAppClientById(appId);
		if (!registeredAppClient) {
			throw "No AppClient registered with Id: " + appId + " !";
		}
		if (registeredAppClient !== appClient) {
			throw "AppClient was not built by the OsgiPortal !";
		}
		var registeredApp = portalContext.getRegisteredAppById(appId);
		if (!registeredApp) {
			throw "AppClient refer to an unknown App !";
		}
	};

	OsgiPortal.model = OsgiPortal.model || {};
	OsgiPortal.model.PortalContext = PortalContext;

})(window.OsgiPortal);