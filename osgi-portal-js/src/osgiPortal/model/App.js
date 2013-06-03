'use strict';

window.OsgiPortal = window.OsgiPortal || {};

(function(OsgiPortal, undefined) {

	/**
	 * App builder. A Portal Application.
	 */
	var App = function(id, symbolicName, version) {
		if (!id || !symbolicName || !version) {
			throw "OsgiPortal.model.App builder need an id, a symbolic name and a version !";
		}
		this.id = id;
		this.symbolicName = symbolicName;
		this.version = version;

		/** Map of (eventType => appId) this App will Listen. */
		this.eventWiring = [];
	};

	App.prototype.toString = function() {
		return "[App#" + this.id + " symbolicName: " + this.symbolicName + " ; version: " + this.version + "]";
	};

	OsgiPortal.model = OsgiPortal.model || {};
	OsgiPortal.model.App = App;

})(window.OsgiPortal);