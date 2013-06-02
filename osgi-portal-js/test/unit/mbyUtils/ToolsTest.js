describe("MbyUtils.Tools unit test.", function() {

	it("Array.remove() test", function() {
		var array = [2, 5, 3, 7, 9];

		expect(array.length).toBe(5);

		var removed = array.remove(5);
		expect(removed).toBe(true);
		expect(array.length).toBe(4);
		expect(array[0]).toBe(3);
		expect(array[2]).toBe(9);

		removed = array.remove(2);
		expect(removed).toBe(true);
		expect(array.length).toBe(3);
		expect(array[0]).toBe(3);
		expect(array[2]).toBe(9);

		removed = array.remove(9);
		expect(removed).toBe(true);
		expect(array.length).toBe(2);
		expect(array[0]).toBe(3);
		expect(array[1]).toBe(7);
	});

});