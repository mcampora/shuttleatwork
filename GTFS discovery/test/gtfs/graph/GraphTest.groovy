package gtfs.graph;

import static org.junit.Assert.*;
import org.junit.Test;
import gtfs.reader.*;

class GraphTest extends GroovyTestCase {
	def rootpath = "${System.properties['user.dir']}/rsc/1Ashuttle/"

	@Test
	public void testBuildGraph() {
		def reader = new CsvFeedReader(rootpath)
		def feed = reader.read()
		Graph        g = new Graph(feed)
		assert       g.edges["TEMPLIERS"] != null
		assert       g.edges["UNKNOWN"] == null
		assert       g.edges.size() == 6
		assert       g.edges["TEMPLIERS"].arcs["GREEN_SIDE"] != null
		assert       g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].trip != null
		assertEquals g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].trip.route_id, "SB"
		assertEquals g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].trip.trip_id, "SBdown01"
		assertEquals g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].trip.trip_headsign, "To Greenside"
		assertEquals g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].arrival.trip_id, "SBdown01"
		assertEquals g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].departure.departure_time, "11:58:00"
		//assert       g.edges["TEMPLIERS"].arcs["GREEN_SIDE"][0].trip.shape != null
	}

	@Test
	public void testFindRoutes() {
		def reader = new CsvFeedReader(rootpath)
		def feed = reader.read()
		Graph g = new Graph(feed)
	}

	@Test
	public void testFindRoute() {
	}

}
