/** 
 * OSGi Portal builder. 
 */
;(function buildOsgiPortal( OsgiPortal, MbyUtils, undefined ) {
  "use strict";

  /** Instance stores a reference to the OSGi Portal Singleton. */
  var _instance;
  
  /** Utils references. */
  var Tools = MbyUtils.Tools;
  
  var Event = MbyUtils.EventKit.Event;
  
  /** Model references. */
  var Reply = OsgiPortal.Model.Reply;
  var App = OsgiPortal.Model.App;
  var AppClient = OsgiPortal.Model.AppClient;
  
  /** Get the Singleton instance if one exists or create one if it doesn't. */
  function getInstance(portalConfiguration) {
    if (! _instance && portalConfiguration) {
      _instance = init(portalConfiguration);
    } else if (portalConfiguration) {
      throw "OsgiPortal is already configured !";
    }

    return _instance;
  }
    
  /** Singleton initialization. */
  function init(portalConfiguration) {

    /** OSGi Portal configuration. */
    if (!portalConfiguration) {
      throw "No OSGi Portal configuration supplied !";
    }
    
    /** Event Hooks configuration. */
    var _portalContext = new OsgiPortal.Model.PortalContext( portalConfiguration );
 
    /** Register a Portal Application and return the corresponding AppClient. */
    function registerPortalApplication( signature ) {  
      var app = loadAppBySignature(signature);
      var appClient = new AppClient( _instance, app, signature );
      
      _portalContext.registerApp(app, appClient);

      return appClient;
    }
    
    function loadAppBySignature( signature ) {
      // TODO implement App loading by signature
      var app = new App( signature, signature, signature );
      app.eventWiring = [{appId: "signature42", topic: "alert"}];
      
      return app;
    }

    /** Fire an event from an AppClient. */
    function fireEventFromAppClient( event, appClient ) {
      // Check AppClient validity
      var registeredApp = _portalContext.checkAppClientValidity(appClient);

      console.log(appClient + " fired " + event + " ...");

      // Fire event on the registered App
      _portalContext.fireEventFromApp(registeredApp.id, event);
    }
    
    /** Do an Action on the Portal. */
    function doActionFromAppClient( action, appClient ) {
       _portalContext.doActionFromAppClient(action, appClient);
    }

    /** Public OsgiPortal API. */
    return {

      /** Register a Portal Application and return the client which will be responsible to communicate with the portal. */
      registerPortalApplication: function( signature ) {
        return registerPortalApplication(signature);
      },

      /** Do an Action on the Portal. */
      doActionFromAppClient: function( action, appClient ) {
        doActionFromAppClient(action, appClient);
      },

      /** Fire an Event from a specific AppClient. */
      fireEventFromAppClient: function( event, appClient ) {
        fireEventFromAppClient(event, appClient);
      }

    };
  }

  OsgiPortal.getInstance = function( portalConfiguration ) {
      return getInstance(portalConfiguration);
  };

})(window.OsgiPortal = window.OsgiPortal || {}, window.MbyUtils);
