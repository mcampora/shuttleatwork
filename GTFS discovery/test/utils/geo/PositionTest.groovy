package utils.geo;

import static org.junit.Assert.*;

class PositionTest extends GroovyTestCase {

	public void testDistance() {
		def a = new Position(0, 0)

		assertEquals(12742.0, a.distance(new Position(0, 2)), 0.0)
		assertEquals(6371.0, a.distance(new Position(0, 1)), 0.0)
		assertEquals(6371.0, a.distance(new Position(0, -1)), 0.0)
		assertEquals(12742.0, a.distance(new Position(0, -2)), 0.0)

		assertEquals(12742.0, a.distance(new Position(-2, 0)), 0.0)
		assertEquals(6371.0, a.distance(new Position(-1, 0)), 0.0)
		assertEquals(6371.0, a.distance(new Position(1, 0)), 0.0)
		assertEquals(12742.0, a.distance(new Position(2, 0)), 0.0)

		def east = new Position(0, 10)		
		def west = new Position(0, -10)		

		assert (long)new Position(5,10).distance(east) == 8175
		assert (long)new Position(5,10).distance(west) > 8175
		
		assert (long)new Position(0,15).distance(east) == 8175
		assert (long)new Position(0,15).distance(west) > 8175
		
		assert (long)new Position(-5,10).distance(east) == 8175
		assert (long)new Position(-5,10).distance(west) > 8175
		
		assert (long)new Position(0,5).distance(east) == 8175
		assert (long)new Position(0,5).distance(west) > 8175
		
		}
}
