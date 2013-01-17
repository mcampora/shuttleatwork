package utils.csv;

import static org.junit.Assert.*;

class ReaderTest extends GroovyTestCase {
	// class corresponding to one line
	static class A {
		def a
		def b
	}

	public void testTranslateLine() {
		// define headers
		def h = ["a", "b"]
		// define a line
		def l = "1,2"

		//1. create a dynamic object
		def ref = new Object()
		ref.metaClass.a = "1"
		ref.metaClass.b = "2"
		def r = new Reader()
		def res = r.translateLine(h, l)
		assertEquals( res.a, ref.a )
		assertEquals( res.b, ref.b )

		//2. create a specific object
		ref = new A()
		ref.a = "1"
		ref.b = "2"
		r = new Reader(A, null)
		res = r.translateLine(h, l)
		assertEquals( res.a, ref.a )
		assertEquals( res.b, ref.b )

		//3. translate 'b' into an integer
		r.closure = { it.b = it.b.toInteger() }
		res = r.translateLine(h, l)
		assertFalse( res.b == ref.b )
		assertTrue( res.b instanceof Integer )
	}

}
