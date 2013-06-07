'use strict';

var MbyUtils = window.MbyUtils;

window.OsgiPortal = window.OsgiPortal || {};

(function(OsgiPortal, MbyUtils, undefined) {

	var Tools = MbyUtils.Tools;

	var Event = MbyUtils.event.Event;

	var Action = OsgiPortal.model.Action;

	/**
	 * AppClient builder. A Portal App Client, the link between the osgiPortal and the Portal App.
	 */
	var AppClient = function(osgiPortal, app, signature) {
		if (!osgiPortal || !app || !signature) {
			throw "OsgiPortal.model.AppClient builder need the OsgiPortal ref, the App ref name and a signature !";
		}
		this.osgiPortal = osgiPortal;
		this.appId = app.id;
		this.signature = signature;

		this.replyHooks = [];

		/** Array of registered listeners by App symbolicName and by Topic. */
		this.appEventListeners = null;
	};

	/** Register an Event hook on an other App for a topic. */
	AppClient.prototype.registerAppEventHook = function(appSymbolicName, topic, hook) {
		if (!Tools.isFunction(hook)) {
			throw "Cannot register App Event Hook : supplied Hook is not a function !";
		}

		if (!this.appEventListeners) {
			throw "This AppClient is not authorized to register EventHooks !";
		}

		if (!this.appEventListeners[appSymbolicName]) {
			throw "This AppClient is not authorized to register EventHooks on App with symbolicName: ["
					+ appSymbolicName + "] !";
		}

		if (!this.appEventListeners[appSymbolicName][topic]) {
			throw "This AppClient is not authorized to register EventHooks on App with symbolicName: ["
					+ appSymbolicName + "] for topic: [" + topic + "] !";
		}

		var listener = this.appEventListeners[appSymbolicName][topic];

		if (listener.callback) {
			console.log("Replacing " + this + " app event hook on App with symbolicName: [" + appSymbolicName
					+ "] for topic: [" + topic + "].");
		}

		listener.callback = hook;
	};

	/** Register a Reply hook wich will be called by the Portal. */
	AppClient.prototype.registerReplyHook = function(type, hook) {
		if (!Tools.isFunction(hook)) {
			throw "Cannot register Reply Hook : supplied Hook is not a function !";
		}

		var oldHook = this.replyHooks[type];
		if (oldHook) {
			throw "Replacing " + this + " reply hook for type: [" + type + "].";
		}

		this.replyHooks[type] = hook;
	};

	/** Fire a generic event. */
	AppClient.prototype.fireEvent = function(topic, properties) {
		this.osgiPortal.fireEventFromAppClient(this, new Event(topic, properties));
	};

	/** Do an Action on the portal. */
	AppClient.prototype.doAction = function(type, properties) {
		this.osgiPortal.doActionFromAppClient(new Action(this, type, properties));
	};

	/** Fire an Alert event. */
	AppClient.prototype.alert = function(message) {
		this.osgiPortal.fireEventFromAppClient(this, new Event("alert", {
			message : message
		}));
	};

	AppClient.prototype.toString = function() {
		return "[AppClient#" + this.appId + "]";
	};

	OsgiPortal.model = OsgiPortal.model || {};
	OsgiPortal.model.AppClient = AppClient;

})(window.OsgiPortal, MbyUtils);