describe("OsgiPortal.model.Action unit test.", function() {

	it("Builder test", function() {
		var props = {
			val1 : 'val1',
			val2 : 'val2'
		};
		var client = {
			client : 'client'
		};
		var a = new OsgiPortal.model.Action('testAction', props, client);

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.type).toBe('testAction');
		expect(a.properties).toBe(props);
		expect(a.appClient).toBe(client);
	});

	it("Builder test with undefined type", function() {
		expect(function() {
			new OsgiPortal.model.Action();
		}).toThrow();

	});

});