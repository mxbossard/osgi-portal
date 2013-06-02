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

	it("Builder test with null callback", function() {
		var a = new MbyUtils.event.EventListener('testEventListener', null);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.topic).toBe('testEventListener');
		expect(a.callback).toBe(null);

	});

	it("Builder test with undefined topic", function() {
		expect(function() {
			new MbyUtils.event.EventListener();
		}).toThrow();

	});

});