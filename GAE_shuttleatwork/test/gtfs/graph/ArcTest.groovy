package gtfs.graph;

import groovy.util.GroovyTestCase;

class ArcTest extends GroovyTestCase {
	public void testFindTime() {
		Arc arc = new Arc("r", "o", "d", )
		arc.addTime("t", "10:00:00", "")
		arc.addTime("t", "09:00:00", "")
		arc.addTime("t", "11:00:00", "")
		arc.addTime("t", "08:00:00", "")
		
		def a = arc.findTimes("07:00:05")
		def ts = a.getTimes()
		assertEquals ts.size(), 3
		assertEquals ts.first().departure_time, "08:00:00"
		assertEquals ts.last().departure_time, "10:00:00"
		
		a = arc.findTimes("08:00:05")
		ts = a.getTimes()
		assertEquals ts.size(), 3
		assertEquals ts.first().departure_time, "09:00:00"
		assertEquals ts.last().departure_time, "11:00:00"
		
		a = arc.findTimes("09:00:05")
		ts = a.getTimes()
		assertEquals ts.size(), 2
		assertEquals ts.first().departure_time, "10:00:00"
		assertEquals ts.last().departure_time, "11:00:00"
		
		a = arc.findTimes("10:00:05")
		ts = a.getTimes()
		assertEquals ts.size(), 1
		assertEquals ts.first().departure_time, "11:00:00"
		assertEquals ts.last().departure_time, "11:00:00"
		
		a = arc.findTimes("11:00:05")
		ts = a.getTimes()
		assertEquals ts.size(), 3
		assertEquals ts.first().departure_time, "08:00:00"
		assertEquals ts.last().departure_time, "10:00:00"
		assertEquals ts.first().nextDay, true
		
	}
}
