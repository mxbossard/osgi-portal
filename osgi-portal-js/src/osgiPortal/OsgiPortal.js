'use strict';

var Tools = window.MbyUtils.Tools;

window.OsgiPortal = window.OsgiPortal || {};

/**
 * OSGi Portal builder.
 */
(function(OsgiPortal, undefined) {

	var AppClient = OsgiPortal.model.AppClient;
	var PortalContext = OsgiPortal.model.PortalContext;

	/** Instance stores a reference to the OSGi Portal Singleton. */
	var _instance = null;

	/** Hook in charge to load an App by its signature. */
	var _appLoadingHook = null;

	/** Get the Singleton instance if one exists or create one if it doesn't. */
	function getInstance(portalConfiguration) {
		if (!_instance && portalConfiguration) {
			_instance = init(portalConfiguration);
		} else if (portalConfiguration) {
			console.log("WARN: OsgiPortal is already configured !");
		}

		return _instance;
	}

	/** Singleton initialization. */
	function init(portalConfiguration) {

		/** OSGi Portal configuration. */
		if (!portalConfiguration) {
			throw "No OSGi Portal configuration supplied for OsgiPortal initialization !";
		}

		var appLoadingHook = portalConfiguration.appLoadingHook;
		if (!appLoadingHook || !Tools.isFunction(appLoadingHook)) {
			throw "OSGi Portal configuration need an 'appLoadingHook' to be able to load App by signature !";
		}

		_appLoadingHook = appLoadingHook;

		/** Event Hooks configuration. */
		var _portalContext = new PortalContext(portalConfiguration);

		/** Register a Portal Application and return the corresponding AppClient. */
		function registerPortalApplication(signature) {
			var app = loadAppBySignature(signature);
			var appClient = new AppClient(_instance, app, signature);

			_portalContext.registerApp(app, appClient);

			return appClient;
		}

		function loadAppBySignature(signature) {
			var app = _appLoadingHook(signature);

			return app;
		}

		/** Fire an event from an AppClient. */
		function fireEventFromAppClient(appClient, event) {

			// Pass the Event to the context
			_portalContext.fireEventFromAppClient(appClient, event);
		}

		/** Do an Action on the Portal. */
		function doActionFromAppClient(appClient, action) {

			// Pass the action to the context
			_portalContext.doActionFromAppClient(appClient, action);
		}

		/** Public OsgiPortal API. */
		return {

			/**
			 * Register a Portal Application and return the client which will be responsible to communicate with the
			 * portal.
			 */
			registerPortalApplication : function(signature) {
				return registerPortalApplication(signature);
			},

			/** Do an Action on the Portal. */
			doActionFromAppClient : function(appClient, action) {
				doActionFromAppClient(appClient, action);
			},

			/** Fire an Event from a specific AppClient. */
			fireEventFromAppClient : function(appClient, event) {
				fireEventFromAppClient(appClient, event);
			}

		};
	}

	OsgiPortal.getInstance = function(portalConfiguration) {
		return getInstance(portalConfiguration);
	};

})(window.OsgiPortal);