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

		target.addEventListener(new MbyUtils.event.EventListener('incrementValue', function() {
		}));

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

	it("fireEvent() test with regexp listener", function() {
		var target = new MbyUtils.event.EventTarget('testEventTarget');

		// Fire an event will increment testValue.
		var testValue = 0;
		var testCallback = function() {
			testValue++;
		};
		
		var testCallback2 = function() {
			testValue = testValue + 10;
		};
		
		var testCallback3 = function() {
			testValue = testValue + 100;
		};
		
		var testCallback4 = function() {
			testValue = testValue + 1000;
		};
		
		var testCallback5 = function() {
			testValue = testValue + 10000;
		};
		
		target.addEventListener(new MbyUtils.event.EventListener('incrementValue', testCallback));
		target.addEventListener(new MbyUtils.event.EventListener('.*Value', testCallback2));
		target.addEventListener(new MbyUtils.event.EventListener('.*value', testCallback3));
		target.addEventListener(new MbyUtils.event.EventListener('noTopic', testCallback4));
		target.addEventListener(new MbyUtils.event.EventListener('.*', testCallback5));
		
		expect(testValue).toBe(0);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(10011);

		target.fireEvent(new MbyUtils.event.Event('otherTopic'));

		expect(testValue).toBe(20011);

		target.fireEvent(new MbyUtils.event.Event('incrementValue'));
		target.fireEvent(new MbyUtils.event.Event('incrementValue'));
		target.fireEvent(new MbyUtils.event.Event('incrementValue'));

		expect(testValue).toBe(50044);
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