// Code goes here

var osgiPortal = OsgiPortal.getInstance({
	appLoadingHook : function(signature) {
		var app = new OsgiPortal.model.App('id:' + signature, 'sn:' + signature, 'ver:' + signature);

		if (signature === 'app4') {
			app.eventWiring = [{
				symbolicName : 'sn:app3',
				topic : 'keyUp'
			}];
		}

		return app;
	},
	eventHooks : {
		'alert' : function(event) {
			// On alert events open a jQuery dialog on #alert
			$("#portalAlert span").html(event.properties.message);
		},

		'.*' : function(event) {
			// Match all Event topics
			var props = event.properties;
			var msg = "[App: " + props.sourceSymbolicName + " fired Event#" + event.topic + "] ";
			$("#eventList span").prepend(msg);
		}

	},
	actionHooks : {
		'getPortalValue' : function(action, replyCallback) {
			// On hookedAlert action call callback with action value
			replyCallback(action.type, {
				portalValue : $("#portalValue").val() + ' for App ' + action.properties.sourceSymbolicName
			});
		},

		'refresh_portal' : function(action, replyCallback) {
			// On refresh_portal call a refresh on the page
			window.location = "/portal";
		},

		'.*' : function(action) {
			// Match all Action types
			var props = action.properties;
			var msg = "[App: " + props.sourceSymbolicName + " did Action#" + action.type + "] ";
			$("#actionList span").prepend(msg);
		}

	}

});