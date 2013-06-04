describe("MbyUtils.event.EventListener unit test.", function() {

	it("Builder test", function() {
		var callback = function listenerHook() {
			// callback function
		};

		var a = new MbyUtils.event.EventListener('testEventListener', callback);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.topic).toBe('testEventListener');
		expect(a.callback).toBe(callback);

	});

	it("Builder test with undefined or null topic", function() {
		expect(function() {
			new MbyUtils.event.EventListener();
		}).toThrow();

		expect(function() {
			new MbyUtils.event.EventListener(null);
		}).toThrow();

	});

	it("Builder test with undefined or null callback", function() {
		var a = new MbyUtils.event.EventListener('testEventListener');
		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();

		var b = new MbyUtils.event.EventListener('testEventListener', null);
		expect(b).not.toBeUndefined();
		expect(b).not.toBeNull();
	});

	it("Builder test with bad callback", function() {
		expect(function() {
			var string = 'string';
			new MbyUtils.event.EventListener('testEventListener', string);
		}).toThrow();

	});

});