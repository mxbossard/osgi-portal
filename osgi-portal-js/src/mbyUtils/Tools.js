'use strict';

window.MbyUtils = window.MbyUtils || {};

(function(MbyUtils, undefined) {

	// var stringFormaterPattern = /\{[0-9]+\}/g;
	// var radixBaseTen = 10;

	var trim = function(theValue) {
		// If trim is already define and theValue is not empty we use the trim function else we remove each whitespace
		// at the beginning end ending.
		if (String.prototype.trim) {
			return theValue === null ? '' : String.prototype.trim.call(theValue);
		} else {
			var trimLeft = /^\s+/, trimRight = /\s+$/;
			return theValue === null ? '' : theValue.toString().replace(trimLeft, '').replace(trimRight, '');
		}
	};

	var isNull = function(theValue) {
		return (theValue === null);
	};

	var isUndefined = function(theValue) {
		return (typeof theValue === "undefined");
	};

	var isNullOrUndefined = function(theValue) {
		return isNull(theValue) || isUndefined(theValue);
	};

	var isNumber = function(theValue) {
		if (typeof theValue === "object" && theValue !== null) {
			return (typeof theValue.valueOf() === "number");
		} else {
			return (typeof theValue === "number");
		}
	};

	/** Test if the object supplied is a function. */
	var isFunction = function(functionToCheck) {
		return functionToCheck && Object.prototype.toString.call(functionToCheck) === '[object Function]';
	};

	var isArray = function(arrayToCheck) {
		return arrayToCheck && Object.prototype.toString.call(arrayToCheck) === '[object Array]';
	};

	MbyUtils.Tools = {
		trim : trim,
		isNull : isNull,
		isUndefined : isUndefined,
		isNullOrUndefined : isNullOrUndefined,
		isNumber : isNumber,
		isFunction : isFunction,
		isArray : isArray

	};

})(window.MbyUtils);

if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(searchElement /* , fromIndex */) {
		"use strict";
		if (this == null) {
			throw new TypeError();
		}
		var t = Object(this);
		var len = t.length >>> 0;
		if (len === 0) {
			return -1;
		}
		var n = 0;
		if (arguments.length > 1) {
			n = Number(arguments[1]);
			if (n != n) { // shortcut for verifying if it's NaN
				n = 0;
			} else if (n != 0 && n != Infinity && n != -Infinity) {
				n = (n > 0 || -1) * Math.floor(Math.abs(n));
			}
		}
		if (n >= len) {
			return -1;
		}
		var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
		for (; k < len; k++) {
			if (k in t && t[k] === searchElement) {
				return k;
			}
		}
		return -1;
	};
}

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
