// Code goes here

var osgiPortal = OsgiPortal.getInstance({
	appLoadingHook : function(signature) {
		var app = new OsgiPortal.model.App('id:' + signature, 'sn:' + signature, 'ver:' + signature);

		return app;
	},
	eventHooks : {
		alert : function(event) {
			// On alert events open a jQuery dialog on #alert
			$("#alert").html(event.properties.message);
		}

	},
	actionHooks : {
		hookedAlert : function(action) {
			// On hookedAlert action call callback with action value
			callback(action.properties.val);
		}

	}

});