package utils.geo;

import static org.junit.Assert.*;

class PositionTest extends GroovyTestCase {

	public void testDistance() {
		// length of the equator 40075km
		// 1deg/360 = 111km
		
		def a = new Position(0, 0)

		assert (long)(a.distance(new Position(0, 2))) == 222341
		assert (long)(a.distance(new Position(0, 1))) == 111170
		assert (long)(a.distance(new Position(0, -1))) == 111170
		assert (long)(a.distance(new Position(0, -2))) == 222341

		assert (long)(a.distance(new Position(-2, 0))) == 222341
		assert (long)(a.distance(new Position(-1, 0))) == 111170
		assert (long)(a.distance(new Position(1, 0))) == 111170
		assert (long)(a.distance(new Position(2, 0))) == 222341

		def east = new Position(0, 10)		
		def west = new Position(0, -10)		

		assert (long)new Position(5,10).distance(east) == 555855
		assert (long)new Position(5,10).distance(west) > 555855
		
		assert (long)new Position(0,15).distance(east) == 555855
		assert (long)new Position(0,15).distance(west) > 555855
		
		assert (long)new Position(-5,10).distance(east) == 555855
		assert (long)new Position(-5,10).distance(west) > 555855
		
		assert (long)new Position(0,5).distance(east) == 555855
		assert (long)new Position(0,5).distance(west) > 555855
		
		}
}
