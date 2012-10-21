package utils.csv;

import static org.junit.Assert.*;

class ReaderTest extends GroovyTestCase {
	static class A {
		def a
		def b
	}

	public void testTranslateLine() {
		def oa = new A()
		oa.a = "1"
		oa.b = "2"
		def r = new Reader(A, [:])

		def h = ["a", "b"]
		def l = "1,2"

		def res = r.translateLine(h, l)
		assertEquals( res.a, oa.a )
		assertEquals( res.b, oa.b )

		r.columnmap = ["b": { it.toInteger() }]
		res = r.translateLine(h, l)
		assertFalse( res.b == oa.b )
		assertTrue( res.b instanceof Integer )
	}

}
