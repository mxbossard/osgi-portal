describe("OsgiPortal.model.Reply unit test.", function() {

	it("Builder test", function() {
		var props = {
			val1 : 'val1',
			val2 : 'val2'
		};

		var a = new OsgiPortal.model.Reply('testReply', props);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.type).toBe('testReply');
		expect(a.properties).toBe(props);

	});

	it("Builder test with undefined type", function() {
		expect(function() {
			new OsgiPortal.model.Reply();
		}).toThrow();

	});

	it("Builder test with undefined properties", function() {
		new OsgiPortal.model.Reply('type');
	});

	it("Builder test with not object properties ", function() {
		expect(function() {
			var string = 'string';
			new OsgiPortal.model.Reply('type', string);
		}).toThrow();

	});
});