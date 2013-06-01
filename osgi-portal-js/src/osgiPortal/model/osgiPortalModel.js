/** Add Model nested namespace to OsgiPortal namespace. */
;(function addOsgiPortalNestedModelNamespace( OsgiPortal, MbyUtils, undefined ) {
  "use strict";

  var Tools = MbyUtils.Tools;
  
  var Event = MbyUtils.EventKit.Event;
  var EventListener = MbyUtils.EventKit.EventListener;
  var EventTarget = MbyUtils.EventKit.EventTarget;
  
  /** 
   * Action builder.
   * An Action on the portal.
   */
  var Action = function ( type, properties, appClient ) {
    this.type = type;
    this.properties = properties;
    this.appClient = appClient;

  };

  Action.prototype.isType = function( type ) {
    return this.type === type;
  };

  Action.prototype.toString = function( ) {
    return "[Action#" + this.type + " properties: " + this.properties + " from " + this.appClient  + "]";
  };
    
  /** 
   * Reply builder.
   * A Reply of the portal.
   */
  var Reply = function ( type, properties ) {
    this.type = type;
    this.properties = properties;

  };
  
  Reply.prototype.isType = function( type ) {
    return this.type === type;
  };
    
  Reply.prototype.toString = function( ) {
    return "[Reply#" + this.type + " properties: " + this.properties + "]";
  };
    
  /**
   * App builder.
   * A Portal Application.
   */
  var App = function ( id, symbolicName, version ) {
    this.id = id;
    this.symbolicName = symbolicName;
    this.version = version;

    /** List of (eventType => appId) this App will Listen. */
    this.eventWiring = [];
  };

  App.prototype.toString = function( ) {
    return "[App#" + this.id + " symbolicName: " + this.symbolicName + " ; version: " + this.version + "]";
  };

  /** 
   * AppClient builder.
   * A Portal App Client, the link between the osgiPortal and the Portal App. 
   */
  var AppClient = function ( osgiPortal, app, signature ) {
    this.osgiPortal = osgiPortal;
    this.appId = app.id;
    this.signature = signature;
    
    this.replyHooks = [];
    
    /** Array of registered listeners by App symbolicName and by Topic. */
    this.appEventListeners = null;
  };
  
  /** Register an Event hook on an other App for a topic. */
  AppClient.prototype.registerAppEventHook = function( appSymbolicName, topic, hook ) {
    if (!Tools.isFunction(hook)) {
      throw "Cannot register Event Hook : supplied Hook is not a function !";
    }
    
    if (! this.appEventListeners) {
      throw "This AppClient is not authorized to register EventHooks !";
    }
    
    if (! this.appEventListeners[appSymbolicName]) {
      throw "This AppClient is not authorized to register EventHooks on App with symbolicName: [" + appSymbolicName + "] !";
    }
    
    if (! this.appEventListeners[appSymbolicName][topic]) {
      throw "This AppClient is not authorized to register EventHooks on App with symbolicName: [" 
        + appSymbolicName + "] for topic: [" + topic + "] !";
    }
    
    var listener = this.appEventListeners[appSymbolicName][topic];

    if (listener.callback) {
      console.log("Replacing " + this + " app event hook on App with symbolicName: [" 
        + appSymbolicName + "] for topic: [" + topic + "].");
    }
    
    listener.callback = hook;
  };
  
  /** Register a Reply hook wich will be called by the Portal. */
  AppClient.prototype.registerReplyHook = function( topic, hook ) {
    if (!Tools.isFunction(hook)) {
      throw "Cannot register Reply Hook : supplied Hook is not a function !";
    }
    
    var oldHook = this.replyHooks[topic];
    if (oldHook) {
      throw "Replacing " + this + " reply hook for topic: [" + topic + "].";
    }
    
    this.replyHooks[topic] = hook;
  };

  /** Call the Hook associated to the Reply. */
  AppClient.prototype.fireReply = function( reply ) {
    var hook = this.replyHooks[reply.type];
    
    if (hook) {
      // Call hook
      hook(reply);
    } else {
      console.log("No Hook registered for incoming " + reply + " to " + this + ".");
    }
  };
  
  /** Do an Action on the portal. */
  AppClient.prototype.doAction = function( type, value ) {
    this.osgiPortal.doActionFromAppClient(new Action(type, value, this), this);
  };

  /** Fire a generic event. */
  AppClient.prototype.fireEvent = function( type, value, source, target ) {
    this.osgiPortal.fireEventFromAppClient(new Event(type, value, source, target), this);
  };

  /** Fire an Alert event. */
  AppClient.prototype.alert = function( message ) {
    this.osgiPortal.fireEventFromAppClient(new Event("alert", message), this);
  };

  AppClient.prototype.toString = function( ) {
    return "[AppClient#" + this.appId + "]";
  };
  
  /**
   * PortalContext builder.
   * The OSGi Portal context.
   */
  var PortalContext = function( configuration ) {

    if (!configuration) {
      throw "No configuration supplied to PortalContext !";
    }

    /** Event Hooks configuration. */
    this.portalEventHooks = [];
    
    /** Action Hooks configuration. */
    this.portalActionHooks = [];

    /** Registered Apps. */
    this.registeredApps = [];
    
    /** Registered AppClients. */
    this.registeredAppClients = [];
    
    /** List of each App EventTarget wich allow the App to fire Events. */
    this.appEventTargets = [];
    
    var eventHooks = configuration.eventHooks;
    if (!eventHooks) {
      eventHooks = [];
    }
    this.portalEventHooks = eventHooks;
    
    var actionHooks = configuration.actionHooks;
    if (!actionHooks) {
      actionHooks = [];
    }
    this.portalActionHooks = actionHooks;
  };

  /** Retrieve an App by its Id. */
  PortalContext.prototype.getRegisteredAppById = function( appId ) {
    var app = this.registeredApps[appId];

    return app;
  };
  
    /** Retrieve an App by its Id. */
  PortalContext.prototype.getRegisteredAppBySymbolicName = function( symbolicName ) {
    for (var k = 0; k < this.registeredApps.length; ++k) {
      var app = this.registeredApps[k];
      if (app && app.symbolicName === symbolicName) {
        return app;
      }
    }

    return null;
  };
      
  /** Retrieve an AppClient by its Id. */
  PortalContext.prototype.getRegisteredAppClientById = function( appId ) {
    var appClient = this.registeredAppClients[appId];

    return appClient;
  };
      
  /** Retrieve an App EventTarget. */
  PortalContext.prototype.getAppEventTarget = function( appId ) {
    var eventTarget = this.appEventTargets[appId];
    
    if (! eventTarget) {
      eventTarget = new EventTarget( "App#" + appId );
      this.appEventTargets[appId] = eventTarget;
    }

    return eventTarget;
  };
      
  /** Fire an event from an App. */
  PortalContext.prototype.fireEventFromApp = function( appId, event ) {
    var appEventTarget = this.getAppEventTarget(appId);
    appEventTarget.fireEvent(event);
  };
    
  /** Add an App and its Client in the context. */
  PortalContext.prototype.registerApp = function( app, appClient ) {
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

    var appEventTarget = this.getAppEventTarget(app.id);

    // Register Portal EventHooks on App
    for (var eventTopic in this.portalEventHooks) {
      if (this.portalEventHooks.hasOwnProperty(eventTopic)) {
        var eventHook = this.portalEventHooks[eventTopic];
        if (eventHook && Tools.isFunction(eventHook)) {
          var hookListener = new EventListener( eventTopic, eventHook );
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
    for (var k = 0; k < app.eventWiring.length; ++k) {
      var wire = app.eventWiring[k];
      
      // Listener info
      var topic = wire.topic;
      var symbolicName = wire.symbolicName;
      var appToListen = this.getRegisteredAppBySymbolicName(symbolicName);
      
      if (appToListen) {
        var appToListenEventTarget = this.getAppEventTarget(appToListen.id);
        // Build a listener with a null callback for this wire.
        var wireListener = new EventListener( topic, null );
        appToListenEventTarget.addEventListener(wireListener);
        
        // Add listener to AppClient
        var clientListeners = appClient.appEventListeners;
        if (! clientListeners) {
          clientListeners = [];
          appClient.appEventListeners = clientListeners;
        }
        if (! clientListeners[symbolicName]) {
          clientListeners[symbolicName] = [];
          appClient.appEventListeners[symbolicName] = clientListeners[symbolicName];
        }
          
        appClient.appEventListeners[symbolicName][topic] = wireListener;
        
        console.log(app + " wiring configured to listen " + appToListen + " on topic: [" + topic + "].");
      }
    }
    
  };

  /** Remove an App from context. */
  PortalContext.prototype.unregisterAppById = function( appId ) {
    var removed = this.registeredApps.remove(appId);
    if (removed) {
      console.log("App#" + appId + " unregistered.");
    } else {
      throw "App#" + appId + " wasn't previously registered !";
    }

    removed = this.registeredAppClients.remove(appId);
    if (removed) {
      console.log("AppClient#" + appId + " unregistered.");
    } else {
      throw "AppClient#" + appId + " wasn't previously registered !";
    }
  };

  /** 
   * Check the validity of an AppClient (do we built it ?).
   * @return corresponding registered App
   */
  PortalContext.prototype.checkAppClientValidity = function( appClient ) {
    if (!appClient) {
      throw "AppClient is null or undefined !";
    }
    
    var appId = appClient.appId;
    var registeredAppClient = this.getRegisteredAppClientById(appId);
    if (!registeredAppClient) {
      throw "No AppClient registered with Id: " + appId + " !";
    }
    if (registeredAppClient !== appClient) {
      throw "AppClient was not built by the OsgiPortal !";
    }
    var registeredApp = this.getRegisteredAppById(appId);
    if (!registeredApp) {
      throw "AppClient refer to an unknown App !";
    }
    
    return registeredApp;
  };
  
  /** Do an Action on the Portal. */
  PortalContext.prototype.doActionFromAppClient = function( action, appClient ) {
    // Check AppClient validity
    var registeredApp = this.checkAppClientValidity(appClient);
    
    console.log(appClient + " doing " + action + " ...");
    
    var actionType = action.type;
    var actionHook = this.portalActionHooks[actionType];
    
    if (actionHook) {
      if (!Tools.isFunction(actionHook)) {
        throw "Configured Portal Action Hook for type " + actionType + " is not a function !";
      }
      
      // Call Action Hook passing the callback for the Reply
      actionHook(action, function( replyValue, targetClientIds ) {
        if (!replyValue) {
          console.log("Action Hook callback for Action type " + actionType + " did not return a value !");
        } else {
          // We have a Reply value
          var reply = new Reply(action.type, replyValue);
          
          if (targetClientIds) {
            if (!Tools.isArray(targetClientIds)) {
              throw "Action Hook callback for Action type " + actionType + " was not passed an array for targetClientIds parameter !";
            }
            // Send the reply to multiple clients
            for (var k = 0; k < targetClientIds.length; ++k) {
              var targetClientId = targetClientIds[k];
              if (targetClientId) {
                var targetAppClient = this.getRegisteredAppClientById(targetClientId);
                if (targetAppClient) {
                  targetAppClient.fireReply(reply);
                } else {
                  throw "Unable to send Replies cause no AppClient registered with Id: " + targetClientId + " !";
                }
              } else {
                throw "Unable to determine targetClientId: " + targetClientId + " !";
              }
            }
          } else {
            // Send the Reply to the client which did the Action
            appClient.fireReply(reply);
          }
        }
      });
    }

  };
    
  OsgiPortal.Model = {
    Action: Action,
    Reply: Reply,
    App: App,
    AppClient: AppClient,
    PortalContext: PortalContext
  };
  
})(window.OsgiPortal = window.OsgiPortal || {}, window.MbyUtils);