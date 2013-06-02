'use strict';

window.MbyUtils = window.MbyUtils || {};

(function(MbyUtils, window, undefined) {
	var loggerNames = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time",
			"timeEnd", "count", "trace", "profile", "profileEnd"];

	var disableLoggers = false;

	// Initialize the loggers
	function initLogger() {
		var console;
		// If no console exist
		if (MbyUtils.Tools.isNullOrUndefined(window.console)) {
			window.console = {};
		}

		console = window.console;

		var nullFunction = function() {
		};
		for ( var i = 0; i < loggerNames.length; ++i) {
			// Initialize not existing loggers with null function
			var logger = console[loggerNames[i]];
			if (disableLoggers || MbyUtils.Tools.isNullOrUndefined(logger)) {
				console[loggerNames[i]] = nullFunction;
			}
		}

		return console;
	};

	var _console = initLogger();

	window.log = function(theObj) {
		_console.log(theObj);
	};

	window.debug = function(theObj) {
		_console.debug(theObj);
	};

	window.info = function(theObj) {
		_console.info(theObj);
	};

	window.warn = function(theObj) {
		_console.warn(theObj);
	};

	window.error = function(theObj) {
		_console.error(theObj);
	};

	MbyUtils.Logger = {
		log : window.log,
		debug : window.debug,
		info : window.info,
		warn : window.warn,
		error : window.error
	};

})(window.MbyUtils, window);

/**
 * Remove an object from the array.
 * 
 * @memberof Array.prototype
 * @param {object}
 *            object the object to remove.
 */
Array.prototype.remove = function(object) {
	var i = this.indexOf(object);
	if (i !== -1) {
		this.splice(i, 1);
		return true;
	}
	return false;
};

/**
 * Check if the array contains a particular object.
 * 
 * @memberof Array.prototype
 * @param {object}
 *            object the object to check if the array is containing it.
 */
Array.prototype.contains = function(object) {
	var result = false, indexElem;
	indexElem = this.indexOf(object);
	if (indexElem !== -1) {
		result = true;
	}
	return result;
};