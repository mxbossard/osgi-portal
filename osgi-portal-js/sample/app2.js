var osgiPortal = window.parent.OsgiPortal.getInstance();
var appClient = osgiPortal.registerPortalApplication("app2");

appClient.registerReplyHook("getPortalValue", function(reply) {
	console.log("getPortalValue Reply called");
	document.getElementById("portalValue").innerHTML = reply.properties.portalValue;
});