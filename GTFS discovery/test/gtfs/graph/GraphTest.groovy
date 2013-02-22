package gtfs.graph;

import static org.junit.Assert.*;
import org.junit.Test;
import gtfs.reader.*;

class GraphTest extends GroovyTestCase {
	def rootpath = "${System.properties['user.dir']}/rsc"
	def name = "1Ashuttle"

	@Test
	public void testBuildGraph() {
		println "testBuildGraph"
		def reader = new CsvFeedReader(rootpath, name)
		def feed = reader.read()
		Graph        g = new Graph(feed)
		assert       g.edges["TEMPLIERS"] != null
		assert       g.edges["UNKNOWN"] == null
		assert       g.edges.size() == 6

		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"] != null
		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"].size() == 1
		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"][0].route_id == "SB"

		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE"] != null
		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE"].size() == 1
		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE"][0].route_id == "SB"

		assertEquals g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE"][0].times[7].trip_id, "SBdown08"
		assertEquals g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE"][0].times[7].departure_time, "14:13:00"
	}

	@Test
	public void testFindRoutes() {
		def reader = new CsvFeedReader(rootpath, name)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutes("GREEN_SIDE")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 8
		assert r["SB"][0].times[0].departure_time.equals("11:45:00")
	}

	@Test
	public void testFindRoutesWithDate() {
		def reader = new CsvFeedReader(rootpath, name)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutes("GREEN_SIDE", "11:46:00")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 8
		assert r["SB"][0].times[0].departure_time.equals("12:00:00")
	}

	@Test
	public void testFindRoute() {
	}

}
