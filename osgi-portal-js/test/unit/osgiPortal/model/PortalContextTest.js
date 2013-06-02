describe("OsgiPortal.model.PortalContext unit test.", function() {

	var osgiPortalMock = {};
	var appMock = {
		id : 'appMockId'
	};

	it("Builder test", function() {
		var configuration = {};
		var a = new OsgiPortal.model.PortalContext(configuration);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
	});

	it("Builder test with undefined configuration", function() {
		expect(function() {
			new OsgiPortal.model.PortalContext();
		}).toThrow();
	});

	it("registerApp() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');
		context.registerApp(app, appClient);
	});

	it("registerApp() test without App", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		expect(function() {
			context.registerApp();
		}).toThrow();

	});

	it("registerApp() test without AppClient", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);
		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');

		expect(function() {
			context.registerApp(app);
		}).toThrow();

	});

	it("registerApp() test with App and AppClient not synchronized", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);
		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');
		appClient.appId = "anotherId";

		expect(function() {
			context.registerApp(app, appClient);
		}).toThrow();

	});

	it("getRegisteredAppById() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var appId = 42;
		var app = new OsgiPortal.model.App(appId, 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');

		var retrievedApp = context.getRegisteredAppById(appId);
		expect(retrievedApp).toBeNull();

		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppById(appId);
		expect(retrievedApp).toBe(app);
	});

	it("getRegisteredAppBySymbolicName() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var symbolicName = 'symbolic42';
		var app = new OsgiPortal.model.App(12, symbolicName, 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');

		var retrievedApp = context.getRegisteredAppBySymbolicName(symbolicName);
		expect(retrievedApp).toBeNull();

		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppBySymbolicName(symbolicName);
		expect(retrievedApp).toBe(app);
	});

	it("getRegisteredAppClientById() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var appId = 42;
		var app = new OsgiPortal.model.App(appId, 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');

		var retrievedAppClient = context.getRegisteredAppClientById(appId);
		expect(retrievedAppClient).toBeNull();

		context.registerApp(app, appClient);

		var retrievedAppClient = context.getRegisteredAppClientById(appId);
		expect(retrievedAppClient).toBe(appClient);
	});

	it("getAppEventTarget() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var eventTarget1 = context.getAppEventTarget(42);
		expect(eventTarget1).not.toBeUndefined();
		expect(eventTarget1).not.toBeNull();

		var eventTarget2 = context.getAppEventTarget(17);
		expect(eventTarget2).not.toBeUndefined();
		expect(eventTarget2).not.toBeNull();

		var eventTarget3 = context.getAppEventTarget(42);
		expect(eventTarget3).not.toBeUndefined();
		expect(eventTarget3).not.toBeNull();
		expect(eventTarget3).toBe(eventTarget1);
	});

	it("unregisterApp() test", function() {
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(appMock, app, 'signature');
		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppById('id');
		expect(retrievedApp).toBe(app);

		var retrievedAppClient = context.getRegisteredAppClientById('id');
		expect(retrievedAppClient).toBe(appClient);

		context.unregisterApp(retrievedApp);

		var retrievedApp2 = context.getRegisteredAppById('id');
		expect(retrievedApp2).toBeNull();

		var retrievedAppClient2 = context.getRegisteredAppClientById('id');
		expect(retrievedAppClient2).toBeNull();
	});

});