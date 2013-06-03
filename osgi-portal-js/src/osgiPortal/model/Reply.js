'use strict';

window.OsgiPortal = window.OsgiPortal || {};

(function(OsgiPortal, undefined) {

	/**
	 * Reply builder. A Reply of the portal.
	 */
	var Reply = function(type, properties) {
		if (!type) {
			throw "OsgiPortal.model.Reply builder need a type !";
		}

		if (properties && typeof properties !== 'object') {
			throw "Reply properties need to be an Object !";
		}

		this.type = type;
		this.properties = properties || {};
	};

	Reply.prototype.isType = function(type) {
		return this.type === type;
	};

	Reply.prototype.toString = function() {
		return "[Reply#" + this.type + " properties: " + this.properties + "]";
	};

	OsgiPortal.model = OsgiPortal.model || {};
	OsgiPortal.model.Reply = Reply;

})(window.OsgiPortal);
