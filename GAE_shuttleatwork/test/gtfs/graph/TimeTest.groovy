package gtfs.graph;

import groovy.util.GroovyTestCase;

class TimeTest extends GroovyTestCase {
	public void testAfter() {
		Time t1 = new Time("01", "10:00:00", "10:05:00")
		Time t2 = new Time("01", "10:06:00", "10:11:00")
		assert t2.after("10:05:00")
		assert t2.after(t1)	
		assertFalse t1.after(t2)
		assertFalse t1.after("10:06:00")
		assertEquals t1.compareTo(t2), -1
		assertEquals t2.compareTo(t1), 1
		assertEquals t1.compareTo(t1), 0
	}
}
