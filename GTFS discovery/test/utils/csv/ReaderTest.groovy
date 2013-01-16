package utils.csv;

import static org.junit.Assert.*;

class ReaderTest extends GroovyTestCase {
	// class corresponding to one line
	static class A {
		def a
		def b
	}

	public void testTranslateLine() {
		// create a reference object
		def ref = new A()
		ref.a = "1"
		ref.b = "2"
		
		// initialize a reader
		def r = new Reader(A, [:])
		
		// define headers
		def h = ["a", "b"]

		// define a line
		def l = "1,2"

		// get the corresponding object
		def res = r.translateLine(h, l)
		
		// compare to the reference object
		assertEquals( res.a, ref.a )
		assertEquals( res.b, ref.b )

		// second test, translate 'b' into an integer
		r.columnmap = ["b": { it.toInteger() }]
		res = r.translateLine(h, l)
		
		// check that it no longers compare to the reference object
		// and that the translation occured
		assertFalse( res.b == ref.b )
		assertTrue( res.b instanceof Integer )
	}

}
