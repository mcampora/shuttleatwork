package gtfs.graph;

import static org.junit.Assert.*;
import gtfs.reader.*;
import groovy.util.GroovyTestCase;

class GraphTest extends GroovyTestCase {
	def rootpath = "${System.properties['user.dir']}/rsc"
	def sname = "1Ashuttle"

	public void testBuildGraph() {
		println "testBuildGraph"
		def reader = new CsvFeedReader(rootpath, sname)
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

	public void testFindRoutes() {
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutes("GREEN_SIDE")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 8
		assert r["SB"][0].times[0].departure_time.equals("11:45:00")
	}

	public void testFindRoutesWithTime() {
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutesAndTimes("GREEN_SIDE", "11:46:00")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 7
		assert r["SB"][0].times[0].departure_time.equals("12:05:00")

		r = g.findRoutesAndTimes("GREEN_SIDE", "12:06:00")
		assert r["SB"][0].times.size() == 6
		assert r["SB"][0].times[0].departure_time.equals("12:25:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE", "12:26:00")
		assert r["SB"][0].times.size() == 5
		assert r["SB"][0].times[0].departure_time.equals("12:45:00")

		r = g.findRoutesAndTimes("GREEN_SIDE", "12:46:00")
		assert r["SB"][0].times.size() == 4
		assert r["SB"][0].times[0].departure_time.equals("13:05:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE", "13:06:00")
		assert r["SB"][0].times.size() == 3
		assert r["SB"][0].times[0].departure_time.equals("13:20:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE", "13:21:00")
		assert r["SB"][0].times.size() == 2
		assert r["SB"][0].times[0].departure_time.equals("13:40:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE", "13:41:00")
		assert r["SB"][0].times.size() == 1
		assert r["SB"][0].times[0].departure_time.equals("14:00:00")

		r = g.findRoutesAndTimes("GREEN_SIDE", "14:01:00")
		assert r["SB"][0].times.size() == 0
	}
}
