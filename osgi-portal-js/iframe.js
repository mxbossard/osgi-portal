var osgiPortal = parent.OsgiPortal.getInstance();
var appClient = osgiPortal.registerPortalApplication("signature42");

appClient.registerReplyHook("hookedAlert", function( reply ){
  console.log("hookedReplyAlert called");
  document.getElementById("hookedReplyAlert").innerHTML = reply.properties;
});