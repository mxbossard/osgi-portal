'use strict';

var Tools = window.MbyUtils.Tools;

window.MbyUtils = window.MbyUtils || {};

(function(MbyUtils, undefined) {

	/**
	 * EventListener builder. An Event Listener.
	 */
	var EventListener = function(topic, callback) {
		if (!topic) {
			throw "MbyUtils.event.EventListener builder need a topic !";
		}

		if (callback && !Tools.isFunction(callback)) {
			throw "MbyUtils.event.EventListener builder need a function as callback !";
		}

		this.topic = topic;
		this.callback = callback;

	};

	EventListener.prototype.isTopic = function(topic) {
		return this.topic === topic;
	};

	EventListener.prototype.toString = function() {
		return "[EventListener#" + this.topic + "]";
	};

	MbyUtils.event = MbyUtils.event || {};
	MbyUtils.event.EventListener = EventListener;

})(window.MbyUtils);