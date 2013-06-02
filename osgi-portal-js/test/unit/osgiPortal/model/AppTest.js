describe("OsgiPortal.model.App unit test.", function() {

	it("Builder test", function() {

		var a = new OsgiPortal.model.App('testAppId', 'testAppSymbolicName', 'testAppVersion');

		expect(a).not.toBeUndefined();
		expect(a).not.toBeNull();
		expect(a.id).toBe('testAppId');
		expect(a.symbolicName).toBe('testAppSymbolicName');
		expect(a.version).toBe('testAppVersion');
	});

	it("Builder test with undefined id", function() {
		expect(function() {
			new OsgiPortal.model.App();
		}).toThrow();
	});

	it("Builder test with undefined symbolicName", function() {
		expect(function() {
			new OsgiPortal.model.App('testId');
		}).toThrow();
	});

	it("Builder test with undefined version", function() {
		expect(function() {
			new OsgiPortal.model.App('testId', 'testSymbolicName');
		}).toThrow();
	});

});