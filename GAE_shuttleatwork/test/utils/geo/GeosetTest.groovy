package utils.geo;

//import static org.junit.Assert.*;

class GeosetTest extends GroovyTestCase {

	public void testFindClosest() {
		Geoset set = new Geoset()

		set.add(new Position(0, 0), "0")
		set.add(new Position(10, 0), "N")
		set.add(new Position(0, 10), "E")
		set.add(new Position(-10, 0), "S")
		set.add(new Position(0, -10), "W")
		
		assert set.closest(new Position(1,0)) == "0"
		assert set.closest(new Position(1,1)) == "0"
		assert set.closest(new Position(0,1)) == "0"
		assert set.closest(new Position(-1,1)) == "0"
		assert set.closest(new Position(-1,0)) == "0"
		assert set.closest(new Position(-1,-1)) == "0"
		assert set.closest(new Position(0,-1)) == "0"
		assert set.closest(new Position(1,-1)) == "0"
		
		assert set.closest(new Position(1,11)) == "E"
		assert set.closest(new Position(0,11)) == "E"
		assert set.closest(new Position(-1,11)) == "E"
		assert set.closest(new Position(-1,10)) == "E"
		assert set.closest(new Position(-1,9)) == "E"
		assert set.closest(new Position(0,9)) == "E"
		assert set.closest(new Position(1,9)) == "E"
		assert set.closest(new Position(1,10)) == "E"
	}

}
