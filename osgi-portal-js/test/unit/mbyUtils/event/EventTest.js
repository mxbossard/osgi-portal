describe("MbyUtils.event.Event unit test.", function() {

	it("Builder test", function() {
		var props = {
			val1 : 'val1',
			val2 : 'val2'
		};

		var a = new MbyUtils.event.Event('testEvent', props);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.topic).toBe('testEvent');
		expect(a.properties).toBe(props);

	});

	it("Builder test with undefined topic", function() {
		expect(function() {
			new MbyUtils.event.Event();
		}).toThrow();

	});

	it("Builder test with undefined properties", function() {
		new MbyUtils.event.Event('topic');
	});

	it("Builder test with not object properties ", function() {
		expect(function() {
			var string = 'string';
			new MbyUtils.event.Event('topic', string);
		}).toThrow();

	});
});