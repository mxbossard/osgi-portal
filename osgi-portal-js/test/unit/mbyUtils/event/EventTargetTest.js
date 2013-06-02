describe("MbyUtils.event.EventTarget unit test.", function() {

	it("Builder test", function() {
		var a = new MbyUtils.event.EventTarget('testEventTarget');

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.name).toBe('testEventTarget');

	});

	it("Builder test with undefined name", function() {
		expect(function() {
			new MbyUtils.event.EventTarget();
		}).toThrow();

	});

	it("addEventListener() test", function() {
		var target = new MbyUtils.event.EventTarget('testEventTarget');

		target.addEventListener(new MbyUtils.event.EventListener('incrementValue'));

	});

	it("addEventListener() empty EventListener test", function() {
		var target = new MbyUtils.event.EventTarget('testEventTarget');

		expect(target.addEventListener).toThrow();

	});

	it("fireEvent() test", function() {
		var target = new MbyUtils.event.EventTarget('testEventTarget');

		// Fire an event will increment testValue.
		var testValue = 0;
		var testCallback = function() {
			testValue++;
		};

		target.addEventListener(new MbyUtils.event.EventListener('incrementValue', testCallback));

		expect(testValue).toBe(0);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(1);

		target.fireEvent(new MbyUtils.event.Event('otherTopic'));

		expect(testValue).toBe(1);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));
		target.fireEvent(new MbyUtils.event.Event('incrementValue'));
		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(4);
	});

	it("removeEventListener() test", function() {
		var target = new MbyUtils.event.EventTarget('testEventTarget');

		// Fire an event will increment testValue.
		var testValue = 0;
		var testCallback = function() {
			testValue++;
		};
		var incrementListener = new MbyUtils.event.EventListener('incrementValue', testCallback);

		target.addEventListener(incrementListener);

		expect(testValue).toBe(0);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(1);

		target.fireEvent(new MbyUtils.event.Event('otherTopic'));

		expect(testValue).toBe(1);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		target.removeEventListener(incrementListener);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));
		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(2);
	});

});