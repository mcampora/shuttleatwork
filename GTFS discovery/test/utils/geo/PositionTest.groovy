package utils.geo;

import static org.junit.Assert.*;

class PositionTest extends GroovyTestCase {

	public void testDistance() {
		def a = new Position(0, 1)
		def b = new Position(0, 2)
		assertEquals(6371.0, a.distance(b), 0.0)
	}
}
