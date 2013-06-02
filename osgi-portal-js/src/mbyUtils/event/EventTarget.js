'use strict';

window.MbyUtils = window.MbyUtils || {};

(function(MbyUtils, undefined) {

	var Tools = MbyUtils.Tools;

	/**
	 * EventTarget builder. A List of Event Listener on which we can fire Events.
	 */
	var EventTarget = function(name) {
		if (!name) {
			throw "MbyUtils.event.EventTarget builder need a name !";
		}
		this.name = name;
		this.listenersByTopic = [];

	};

	EventTarget.prototype.addEventListener = function(eventListener) {
		if (!eventListener) {
			throw "Trying to add null or undefined EventListener in " + this + " !";
		}

		if (eventListener.callback && !Tools.isFunction(eventListener.callback)) {
			throw "Trying to add eventListener with invalid callback in " + this + " !";
		}

		var topic = eventListener.topic;
		var topicListeners = this.listenersByTopic[topic];
		if (!topicListeners) {
			topicListeners = [];
			this.listenersByTopic[topic] = topicListeners;
		}

		topicListeners.push(eventListener);

		console.log("EventListener " + eventListener + " registered in " + this + " .");
	};

	EventTarget.prototype.removeEventListener = function(eventListener) {
		if (!eventListener) {
			throw "Trying to remove null or undefined EventListener in " + this + " !";
		}

		var topic = eventListener.topic;
		var topicListeners = this.listenersByTopic[topic];

		var removed = false;
		if (topicListeners) {
			removed = topicListeners.remove(eventListener);
		}

		if (removed) {
			console.log("EventListener " + eventListener + " unregistered from " + this + " .");
		} else {
			throw "EventListener " + eventListener + " not found in " + this + " !";
		}
	};

	EventTarget.prototype.fireEvent = function(event) {
		if (!event) {
			throw "Trying to fire null or undefined Event on " + this + " !";
		}

		var topic = event.topic;
		var topicListeners = this.listenersByTopic[topic];

		if (topicListeners) {
			// Call each listener registered
			for ( var k = 0; k < topicListeners.length; ++k) {
				var listener = topicListeners[k];
				if (listener && Tools.isFunction(listener.callback)) {
					// Call listener
					listener.callback(event);
				}
			}
		}

	};

	EventTarget.prototype.toString = function() {
		return "[EventTarget#" + this.name + "]";
	};

	MbyUtils.event = MbyUtils.event || {};
	MbyUtils.event.EventTarget = EventTarget;

})(window.MbyUtils);