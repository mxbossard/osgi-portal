appClient.registerReplyHook("getPortalValue", function(reply) {
	console.log("getPortalValue Reply called");
	document.getElementById("portalValue").innerHTML = reply.properties.portalValue;
});