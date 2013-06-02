describe("OsgiPortal.model.AppClient unit test.", function() {

	var osgiPortalMock = {};
	var appMock = {
		id : 'appMockId'
	};

	it("Builder test", function() {

		var a = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.osgiPortal).toBe(osgiPortalMock);
		expect(a.appId).toBe(appMock.id);
		expect(a.signature).toBe('testAppClientSignature');
	});

	it("Builder test with undefined OsgiPortal", function() {
		expect(function() {
			new OsgiPortal.model.AppClient();
		}).toThrow();
	});

	it("Builder test with undefined App", function() {
		expect(function() {
			new OsgiPortal.model.AppClient(osgiPortalMock);
		}).toThrow();
	});

	it("Builder test with undefined signature", function() {
		expect(function() {
			new OsgiPortal.model.AppClient(osgiPortalMock, appMock);
		}).toThrow();
	});

	it("registerAppEventHook() registration not authorized test", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		var eventHookCallback = function() {
		};

		expect(function() {
			appClient.registerAppEventHook('otherAppSn', 'userAction', eventHookCallback);
		}).toThrow();

	});

	it("registerAppEventHook() registration not authorized for this App test", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');
		appClient.appEventListeners = [];
		var eventHookCallback = function() {
		};

		expect(function() {
			appClient.registerAppEventHook('otherAppSn', 'userAction', eventHookCallback);
		}).toThrow();

	});

	it("registerAppEventHook() registration not authorized for this topic test", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');
		appClient.appEventListeners = [];
		appClient.appEventListeners['otherAppSn'] = [];

		var eventHookCallback = function() {
		};

		expect(function() {
			appClient.registerAppEventHook('otherAppSn', 'userAction', eventHookCallback);
		}).toThrow();

	});

	it("registerAppEventHook() test", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');
		appClient.appEventListeners = [];
		appClient.appEventListeners['otherAppSn'] = [];
		var eventListener = new MbyUtils.event.EventListener('userAction', null);
		appClient.appEventListeners['otherAppSn']['userAction'] = eventListener;

		var eventHookCallback = function() {
		};
		appClient.registerAppEventHook('otherAppSn', 'userAction', eventHookCallback);

		expect(eventListener.callback).toBe(eventHookCallback);
	});

	it("registerAppEventHook() test with empty appSymbolicName", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerAppEventHook();
		}).toThrow();

	});

	it("registerAppEventHook() test with empty Event topic", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerAppEventHook('otherAppSn');
		}).toThrow();

	});

	it("registerAppEventHook() test with empty callback", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerAppEventHook('otherAppSn', 'userAction');
		}).toThrow();

	});

	it("registerAppEventHook() test with not function callback", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerAppEventHook('otherAppSn', 'userAction', null);
		}).toThrow();

	});

	it("registerReplyHook() test", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		var replyHookCallback = function() {
		};
		appClient.registerReplyHook('userAction', replyHookCallback);

	});

	it("registerReplyHook() test with empty reply type", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerReplyHook();
		}).toThrow();

	});

	it("registerReplyHook() test with empty Event topic", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerReplyHook('userAction');
		}).toThrow();

	});

	it("registerReplyHook() test with not function callback", function() {

		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		expect(function() {
			appClient.registerReplyHook('userAction', null);
		}).toThrow();

	});

	it("fireEvent() test", function() {
		var fireEventCount = 0;
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');
		osgiPortalMock.fireEventFromAppClient = function(appClient, event) {
			fireEventCount++;
		};

		appClient.fireEvent(appClient, null);

		expect(fireEventCount).toBe(1);

		appClient.fireEvent(appClient, null);
		appClient.fireEvent(appClient, null);

		expect(fireEventCount).toBe(3);
	});

	it("fireReply() test", function() {
		var appClient = new OsgiPortal.model.AppClient(osgiPortalMock, appMock, 'testAppClientSignature');

		var fireReplyCount = 0;
		var replyHookCallback = function() {
			fireReplyCount++;
		};
		appClient.registerReplyHook('userAction', replyHookCallback);

		var reply1Mock = {
			type : 'userAction'
		};
		var reply2Mock = {
			type : 'otherAction'
		};
		appClient.fireReply(reply1Mock);

		expect(fireReplyCount).toBe(1);

		appClient.fireReply(reply2Mock);
		appClient.fireReply(reply1Mock);

		expect(fireReplyCount).toBe(2);

	});

});