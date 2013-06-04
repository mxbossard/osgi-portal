// The tests need to be executed sequentially
describe("OsgiPortal integration test.", function() {
	var _eventFiredCount = 0;
	var _actionDoneCount = 0;

	var _appClient = null;

	var portalConfig = {
		appLoadingHook : function(signature) {
			var testApp = new OsgiPortal.model.App('id:' + signature, 'sn:' + signature, 'ver:' + signature);

			if (signature === 'test22!') {
				// Specific wiring for 'test22!' app which listen to App with 'id:test42!' on topic 'topic66'
				testApp.eventWiring = [{
					symbolicName : 'sn:test42!',
					topic : 'topic66'
				}];
			}

			if (signature === 'test42!') {
				// Specific wiring for 'test42!' app which listen to App with 'id:test22!' on topic 'topic66' and
				// 'topic99'
				testApp.eventWiring = [{
					symbolicName : 'sn:test22!',
					topic : 'topic77'
				}, {
					symbolicName : 'sn:test22!',
					topic : 'topic66'
				}];
			}

			return testApp;
		},
		eventHooks : {
			'topic99' : function(event) {
				expect(event.topic).toBe('topic99');
				expect(event.properties.val).toBe('val99');
				expect(event.properties.sourceSymbolicName).toBe('sn:test42!');
				expect(event.properties.sourceVersion).toBe('ver:test42!');
				_eventFiredCount++;
			},
			'topic66' : function(event) {
				expect(event.topic).toBe('topic66');
				expect(event.properties.val).toBe('val66');
				expect(event.properties.sourceSymbolicName).toBe('sn:test42!');
				expect(event.properties.sourceVersion).toBe('ver:test42!');
				_eventFiredCount = _eventFiredCount + 2;
			}
		},
		actionHooks : {
			'type99' : function(action) {
				expect(action.type).toBe('type99');
				expect(action.properties.val).toBe('val99_');
				expect(action.properties.sourceSymbolicName).toBe('sn:test42!');
				expect(action.properties.sourceVersion).toBe('ver:test42!');
				_actionDoneCount++;
			},
			'type66' : function(action) {
				expect(action.type).toBe('type66');
				expect(action.properties.val).toBe('val66_');
				expect(action.properties.sourceSymbolicName).toBe('sn:test42!');
				expect(action.properties.sourceVersion).toBe('ver:test42!');
				_actionDoneCount = _actionDoneCount + 2;
			}
		}
	};

	it("getInstance() test without appLoadingHook configured", function() {
		expect(function() {
			OsgiPortal.getInstance({});
		}).toThrow();
	});

	// Build OsgiPortal instance
	it("getInstance() test", function() {
		var a = OsgiPortal.getInstance(portalConfig);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();

	});

	it("singleton test", function() {
		var a = OsgiPortal.getInstance(portalConfig);

		var b = OsgiPortal.getInstance(portalConfig);

		expect(a).toBe(b);

	});

	// Build appClient by registering it
	it("registerPortalApplication() test", function() {
		var portal = OsgiPortal.getInstance(portalConfig);

		var appClient = portal.registerPortalApplication('test42!');
		_appClient = appClient;

		expect(appClient).not.toBeUndefined();
		expect(appClient).not.toBeNull();
		expect(appClient.appId).toBe('id:test42!');
		expect(appClient.signature).toBe('test42!');
		expect(appClient.osgiPortal).toBe(portal);
	});

	// Test firing event from appClient
	it("fireEventFromAppClient() test", function() {
		var portal = OsgiPortal.getInstance(portalConfig);
		var appClient = _appClient;

		// Register a second App wich listen on App1 for Event with topic 'topic66'
		var appClient2 = portal.registerPortalApplication('test22!');
		appClient2.registerAppEventHook('sn:test42!', 'topic66', function(event) {
			expect(event.topic).toBe('topic66');
			expect(event.properties.val).toBe('val66');
			expect(event.properties.sourceSymbolicName).toBe('sn:test42!');
			expect(event.properties.sourceVersion).toBe('ver:test42!');
			_eventFiredCount = _eventFiredCount + 4;
		});

		// Register one hook on same topic but on first App => this should not be called
		appClient.registerAppEventHook('sn:test22!', 'topic66', function(event) {
			expect(event.topic).toBe('topic66');
			expect(event.properties.val).toBe('val66');
			expect(event.properties.sourceSymbolicName).toBe('sn:test22!');
			expect(event.properties.sourceVersion).toBe('ver:test22!');
			_eventFiredCount = _eventFiredCount + 8;
		});

		// Another hook on first App wich listen on second App for topic 'topic99'
		appClient.registerAppEventHook('sn:test22!', 'topic77', function(event) {
			expect(event.topic).toBe('topic77');
			expect(event.properties.val).toBe('val77');
			expect(event.properties.sourceSymbolicName).toBe('sn:test22!');
			expect(event.properties.sourceVersion).toBe('ver:test22!');
			_eventFiredCount = _eventFiredCount + 16;
		});

		// Fire event from portal
		portal.fireEventFromAppClient(appClient, new MbyUtils.event.Event('topic99', {
			val : 'val99'
		}));

		// Fire event from AppClient
		appClient.fireEvent('topic66', {
			val : 'val66'
		});

		// We fired 3 Events
		expect(_eventFiredCount).toBe(7);

		// Fire event from second AppClient
		appClient2.fireEvent('topic77', {
			val : 'val77'
		});

		// We fired 4 Events
		expect(_eventFiredCount).toBe(23);
	});

	// Test doing Action from appClient
	it("doActionFromAppClient() test", function() {
		var portal = OsgiPortal.getInstance(portalConfig);
		var appClient = _appClient;

		// Fire event from portal
		portal.doActionFromAppClient(appClient, new OsgiPortal.model.Action('type99', {
			val : 'val99_'
		}));

		// Fire event from AppClient
		appClient.doAction('type66', {
			val : 'val66_'
		});

		// We did 2 Actions
		expect(_actionDoneCount).toBe(3);
	});

});
