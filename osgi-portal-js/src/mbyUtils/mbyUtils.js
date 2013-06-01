"use strict";

window.MbyUtils = window.MbyUtils || {};

(function buildToolsNestedNamespace( MbyUtils, undefined ) {
  var stringFormaterPattern = /\{[0-9]+\}/g;
  var radixBaseTen = 10;
  
  var trim = function( theValue ) {
    // If trim is already define and theValue is not empty we use the trim function else we remove each whitespace at the beginning end ending.
    if (String.prototype.trim) {
      return theValue === null ? '' : String.prototype.trim.call(theValue);
    } else {
      var trimLeft = /^\s+/, trimRight = /\s+$/;
      return theValue === null ? '' : theValue.toString().replace(trimLeft, '').replace(trimRight, '');
    }
  };

  var isNull = function( theValue ) {
    return (theValue === null);
  };

  var isUndefined = function( theValue ) {
    return (typeof theValue === "undefined");
  };

  var isNullOrUndefined = function( theValue ) {
    return isNull(theValue) || isUndefined(theValue);
  };

  var isNumber = function( theValue ) {
    if (typeof theValue === "object" && theValue !== null) {
      return (typeof theValue.valueOf() === "number");
    } else {
      return (typeof theValue === "number");
    }
  };
  
  /** Test if the object supplied is a function. */
  var isFunction = function( functionToCheck ) {
    return functionToCheck && Object.prototype.toString.call(functionToCheck) === '[object Function]';
  };

  var isArray = function( arrayToCheck ) {
    return arrayToCheck && Object.prototype.toString.call( arrayToCheck ) === '[object Array]';
  };
  
  MbyUtils.Tools = {
    trim: trim,
    isNull: isNull,
    isUndefined: isUndefined,
    isNullOrUndefined: isNullOrUndefined,
    isNumber: isNumber,
    isFunction: isFunction,
    isArray: isArray

  };
  
})(window.MbyUtils);

(function buildLoggerNestedNamespace( MbyUtils, undefined ) {
  var loggerNames = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group",
    "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
  
  var disableLoggers = false;
  var console;
  
  // Initialize the loggers
  (function initLogger( MbyUtils ) {
    // If no console exist
    if (MbyUtils.Tools.isNullOrUndefined(window.console)) {
      window.console = {};
    }
      
    console = window.console;
    
    var nullFunction = function(){};
    for (var i=0; i<loggerNames.length; ++i) {
      // Initialize not existing loggers with null function
      var logger = console[loggerNames[i]];
      if (disableLoggers || MbyUtils.Tools.isNullOrUndefined(logger)) {
        console[loggerNames[i]] = nullFunction;
      }
    }
    
  })(MbyUtils);


  window.log = function( theObj ) {
    console.log(theObj);
  };
  
  window.debug = function( theObj ) {
    console.debug(theObj);
  };
    
  window.info = function( theObj ) {
    console.info(theObj);
  };
  
  window.warn = function( theObj ) {
    console.warn(theObj);
  };
  
  window.error = function( theObj ) {
    console.error(theObj);
  };

  MbyUtils.Logger = {
    log: window.log,
    debug: window.debug,
    info: window.info,
    warn: window.warn,
    error: window.error
  };
  
})(window.MbyUtils);

(function buildEventKitNestedNamespace( MbyUtils, undefined ) {
 
  var Tools = MbyUtils.Tools;
 
  /** 
   * Event builder.
   * An Event through the portal.
   */
  var Event = function ( topic, properties ) {
    this.topic = topic;
    this.properties = properties;
  };

  Event.prototype.toString = function( ) {
    return "[Event#" + this.topic + " properties: " + this.properties + "]";
  };

  /** 
   * EventListener builder.
   * An Event Listener.
   */
  var EventListener = function ( topic, callback ) {
    this.topic = topic;
    this.callback = callback;

  };

  EventListener.prototype.isTopic = function( topic ) {
      return this.topic === topic;
  };

  EventListener.prototype.toString = function( ) {
    return "[EventListener#" + this.topic + "]";
  };

  /** 
   * EventTarget builder.
   * A List of Event Listener on which we can fire Events.
   */
  var EventTarget = function ( name ) {
    this.name = name;
    this.listenersByTopic = [];

  };

  EventTarget.prototype.addEventListener = function( eventListener ) {
    if (! eventListener) {
      throw "Trying to add null or undefined EventListener in " + this + " !";
    }
    
    if (eventListener.callback && ! Tools.isFunction(eventListener.callback)) {
      throw "Trying to add eventListener with invalid callback in " + this + " !";
    }
    
    var topic = eventListener.topic;
    var topicListeners = this.listenersByTopic[topic];
    if (! topicListeners) {
      topicListeners = [];
      this.listenersByTopic[topic] = topicListeners;
    }
    
    topicListeners.push(eventListener);
    
    console.log("EventListener " + eventListener + " registered in " + this + " .");
  };
  
  EventTarget.prototype.removeEventListener = function( eventListener ) {
    if (! eventListener) {
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
   
  EventTarget.prototype.fireEvent = function( event ) {
    if (! event) {
      throw "Trying to fire null or undefined Event on " + this + " !";
    }

    var topic = event.topic;
    var topicListeners = this.listenersByTopic[topic];
    
    if (topicListeners) {
      // Call each listener registered
      for (var k = 0; k < topicListeners.length; ++k) {
        var listener = topicListeners[k];
        if (listener && Tools.isFunction(listener.callback)) {
          // Call listener
          listener.callback(event);
        }
      }
    }
    
  };
   
  EventTarget.prototype.toString = function( ) {
    return "[EventTarget#" + this.name + "]";
  };
  
 MbyUtils.EventKit = {
    Event: Event,
    EventListener: EventListener,
    EventTarget: EventTarget
    
  };
 
})(window.MbyUtils);

/**
 * Remove an object from the array.
 * @memberof Array.prototype
 * @param {object} object the object to remove.
 */
Array.prototype.remove = function( object ) {
  var i = this.indexOf(object);
  if (i !== -1) {
    this.splice(i, 1);
    return true;
  }
  return false;
};

/**
 * Check if the array contains a particular object.
 * @memberof Array.prototype
 * @param {object} object the object to check if the array is containing it.
 */
Array.prototype.contains = function( object ) {
  var result = false, indexElem;
  indexElem = this.indexOf(object);
  if (indexElem !== -1) {
    result = true;
  }
  return result;
};
