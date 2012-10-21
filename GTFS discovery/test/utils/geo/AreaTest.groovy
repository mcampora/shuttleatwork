package utils.geo;

import static org.junit.Assert.*;

class AreaTest extends GroovyTestCase {

	public void testExtendPosition() {
		Area area = new Area()

		area.extend(new Position(0, 0))
		assert area.getMin().lat == 0.0
		assert area.getMin().lon == 0.0
		assert area.getMax().lat == 0.0
		assert area.getMax().lon == 0.0

		area.extend(new Position(-90, -90))
		assert area.getMin().lat == -90.0
		assert area.getMin().lon == -90.0
		assert area.getMax().lat == 0.0
		assert area.getMax().lon == 0.0

		area.extend(new Position(90, 90))
		assert area.getMin().lat == -90.0
		assert area.getMin().lon == -90.0
		assert area.getMax().lat == 90.0
		assert area.getMax().lon == 90.0

		area.extend(new Position(-100, 100))
		assert area.getMin().lat == -100.0
		assert area.getMin().lon == -90.0
		assert area.getMax().lat == 90.0
		assert area.getMax().lon == 100.0
	}

	public void testExtendArea() {
		Area area = new Area()

		Area na = new Area(-90, -90, 0, 0)
		area.extend(na)
		assert area.getMin().lat == na.getMin().lat
		assert area.getMin().lon == na.getMin().lon
		assert area.getMax().lat == na.getMax().lat
		assert area.getMax().lon == na.getMax().lon

		na = new Area(0, 0, 90, 90)
		area.extend(na)
		assert area.getMin().lat == -90.0
		assert area.getMin().lon == -90.0
		assert area.getMax().lat == 90.0
		assert area.getMax().lon == 90.0

		na = new Area(-45, -45, 45, 45)
		area.extend(na)
		assert area.getMin().lat == -90.0
		assert area.getMin().lon == -90.0
		assert area.getMax().lat == 90.0
		assert area.getMax().lon == 90.0
	}
}
