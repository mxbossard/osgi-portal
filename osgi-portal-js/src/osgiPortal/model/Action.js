'use strict';

window.OsgiPortal = window.OsgiPortal || {};

(function(OsgiPortal, undefined) {

	/**
	 * Action builder. An Action on the portal.
	 */
	var Action = function(appClient, type, properties) {
		if (!appClient) {
			throw "Action appClient need to be supplied !";
		}
		
		if (!type) {
			throw "OsgiPortal.model.Action builder need a type !";
		}

		if (properties && typeof properties !== 'object') {
			throw "Action properties need to be an Object !";
		}

		this.type = type;
		this.properties = properties || {};
		this.appClient = appClient;
	};

	Action.prototype.isType = function(type) {
		return this.type === type;
	};

	Action.prototype.toString = function() {
		return "[Action#" + this.type + " properties: " + this.properties + " from " + this.appClient + "]";
	};

	OsgiPortal.model = OsgiPortal.model || {};
	OsgiPortal.model.Action = Action;

})(window.OsgiPortal);