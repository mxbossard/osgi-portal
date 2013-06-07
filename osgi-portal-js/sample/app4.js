var osgiPortal = window.parent.OsgiPortal.getInstance();
var appClient = osgiPortal.registerPortalApplication("app4");

appClient.registerAppEventHook('sn:app3', 'keyUp', function(event) {
	document.getElementById('text').innerHTML = event.properties.text;
});