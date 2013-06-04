'use strict';

window.MbyUtils = window.MbyUtils || {};

(function(MbyUtils, undefined) {

	/**
	 * Event builder. An Event through the portal.
	 */
	var Event = function(topic, properties) {
		if (!topic) {
			throw "MbyUtils.event.Event builder need a topic !";
		}

		if (properties && (typeof properties !== 'object')) {
			throw "Event properties need to be an Object !";
		}

		this.topic = topic;
		this.properties = properties || {};
	};

	Event.prototype.toString = function() {
		return "[Event#" + this.topic + " properties: " + this.properties + "]";
	};

	MbyUtils.event = MbyUtils.event || {};
	MbyUtils.event.Event = Event;

})(window.MbyUtils);