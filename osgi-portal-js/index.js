// Code goes here

var osgiPortal = OsgiPortal.getInstance({
  eventHooks: {
    alert: function( event ) { 
      // On alert events open a jQuery dialog on #alert
      $("#alert").html(event.properties);
      $("#alert").dialog({modal: true});
    }

  },
  
  actionHooks: {
    hookedAlert: function( action, callback ) { 
      // On hookedAlert action call callback with action value
      callback(action.properties);
    }

  }
  
});

var osgiPortal2 = OsgiPortal.getInstance();

var test = osgiPortal == osgiPortal2;
console.log("test: " + test);