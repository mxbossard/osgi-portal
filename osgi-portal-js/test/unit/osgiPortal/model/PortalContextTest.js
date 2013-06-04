describe("OsgiPortal.model.PortalContext unit test.", function() {

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
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');
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
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);
		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');
		appClient.appId = "anotherId";

		expect(function() {
			context.registerApp(app, appClient);
		}).toThrow();

	});

	it("getRegisteredAppById() test", function() {
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var appId = 42;
		var app = new OsgiPortal.model.App(appId, 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');

		var retrievedApp = context.getRegisteredAppById(appId);
		expect(retrievedApp).toBeNull();

		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppById(appId);
		expect(retrievedApp).toBe(app);
	});

	it("getRegisteredAppBySymbolicName() test", function() {
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var symbolicName = 'symbolic42';
		var app = new OsgiPortal.model.App(12, symbolicName, 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');

		var retrievedApp = context.getRegisteredAppBySymbolicName(symbolicName);
		expect(retrievedApp).toBeNull();

		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppBySymbolicName(symbolicName);
		expect(retrievedApp).toBe(app);
	});

	it("getRegisteredAppClientById() test", function() {
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var appId = 42;
		var app = new OsgiPortal.model.App(appId, 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');

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
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');
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

	it("unregisterAppById() test", function() {
		var osgiPortalMock = {};
		var configuration = {};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app = new OsgiPortal.model.App('id', 'symbolicName', 'version');
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, app, 'signature');
		context.registerApp(app, appClient);

		var retrievedApp = context.getRegisteredAppById('id');
		expect(retrievedApp).toBe(app);

		var retrievedAppClient = context.getRegisteredAppClientById('id');
		expect(retrievedAppClient).toBe(appClient);

		context.unregisterAppById('id');

		var retrievedApp2 = context.getRegisteredAppById('id');
		expect(retrievedApp2).toBeNull();

		var retrievedAppClient2 = context.getRegisteredAppClientById('id');
		expect(retrievedAppClient2).toBeNull();
	});

	it("fireEventFromAppClient() test", function() {
		var hookCount = 0;

		var osgiPortalMock = {};
		var configuration = {
			eventHooks : {
				'testEventTopic' : function(event) {
					expect(event.topic).toBe('testEventTopic');
					expect(event.properties.sourceSymbolicName).toBe('symbolicName1');
					expect(event.properties.sourceVersion).toBe('version1');
					expect(event.properties.val).toBe('testPropsVal');
					expect(event.properties.val).toBe('testPropsVal');
					console.log("test Event listened by Portal Event hook");
					hookCount++;
				}
			}
		};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app1 = new OsgiPortal.model.App('id1', 'symbolicName1', 'version1');
		var appClient1 = new OsgiPortal.model.AppClient(osgiPortalMock, app1, 'signature1');
		context.registerApp(app1, appClient1);

		// Configure App2 to listen testEventTopic of App1
		var app2 = new OsgiPortal.model.App('id2', 'symbolicName2', 'version2');
		app2.eventWiring = [{
			symbolicName : 'symbolicName1',
			topic : 'testEventTopic'
		}];
		var appClient2 = new OsgiPortal.model.AppClient(osgiPortalMock, app2, 'signature2');
		context.registerApp(app2, appClient2);
		appClient2.registerAppEventHook('symbolicName1', 'testEventTopic', function(event) {
			expect(event.topic).toBe('testEventTopic');
			expect(event.properties.sourceSymbolicName).toBe('symbolicName1');
			expect(event.properties.sourceVersion).toBe('version1');
			expect(event.properties.val).toBe('testPropsVal');
			console.log("test Event listened by App2 Event hook");
			hookCount = hookCount + 2;
		});

		var event = new MbyUtils.event.Event('testEventTopic', {
			val : 'testPropsVal'
		});
		context.fireEventFromAppClient(appClient1, event);

		// 2 Hooks need to be called
		expect(hookCount).toBe(3);
	});

	it("doActionFromAppClient() test", function() {
		var hookCount = 0;

		var osgiPortalMock = {};
		var configuration = {
			actionHooks : {
				'testActionType' : function(action, replyCallback) {
					expect(action.type).toBe('testActionType');
					expect(action.properties.sourceSymbolicName).toBe('symbolicName1');
					expect(action.properties.sourceVersion).toBe('version1');
					expect(action.properties.val).toBe('testPropsVal42');

					expect(replyCallback).not.toBeNull();
					expect(replyCallback).not.toBeUndefined();

					hookCount++;
				}
			}
		};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app1 = new OsgiPortal.model.App('id1', 'symbolicName1', 'version1');
		var appClient1 = new OsgiPortal.model.AppClient(osgiPortalMock, app1, 'signature1');
		context.registerApp(app1, appClient1);

		var action = new OsgiPortal.model.Action('testActionType', {
			val : 'testPropsVal42'
		});

		context.doActionFromAppClient(appClient1, action);

		// 1 Hook need to be called
		expect(hookCount).toBe(1);
	});

	it("send Reply to AppClient test", function() {
		var hookCount = 0;

		var osgiPortalMock = {};
		var configuration = {
			actionHooks : {
				'testActionType' : function(action, replyCallback) {
					expect(action.type).toBe('testActionType');
					expect(action.properties.sourceSymbolicName).toBe('symbolicName1');
					expect(action.properties.sourceVersion).toBe('version1');
					expect(action.properties.val).toBe('testPropsVal42');

					expect(replyCallback).not.toBeNull();
					expect(replyCallback).not.toBeUndefined();

					hookCount++;

					// Use the replyCallback to send Reply to client
					replyCallback('replyType', {
						val : 'replyVal'
					});

					replyCallback('replyType', {
						val : 'replyVal'
					}, 'id1');

					expect(function() {
						replyCallback('replyType', {
							val : 'replyVal'
						}, 'badId');
					}).toThrow();
				}
			}
		};
		var context = new OsgiPortal.model.PortalContext(configuration);

		var app1 = new OsgiPortal.model.App('id1', 'symbolicName1', 'version1');
		var appClient1 = new OsgiPortal.model.AppClient(osgiPortalMock, app1, 'signature1');
		context.registerApp(app1, appClient1);

		appClient1.registerReplyHook('replyType', function(reply) {
			expect(reply).not.toBeNull();
			expect(reply).not.toBeUndefined();
			expect(reply.type).toBe('replyType');
			expect(reply.properties.val).toBe('replyVal');

			hookCount = hookCount + 2;
		});

		var action = new OsgiPortal.model.Action('testActionType', {
			val : 'testPropsVal42'
		});

		context.doActionFromAppClient(appClient1, action);

		// 1 Hook need to be called
		expect(hookCount).toBe(5);
	});

});