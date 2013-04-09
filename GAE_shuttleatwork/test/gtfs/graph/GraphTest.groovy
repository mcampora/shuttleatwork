package gtfs.graph;

//import static org.junit.Assert.*;
import gtfs.reader.*;
import groovy.util.GroovyTestCase;

class GraphTest extends GroovyTestCase {
	def rootpath = "rsc"
	def sname = "1Ashuttle"

	public void testBuildGraph() {
		println "testBuildGraph"
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph        g = new Graph(feed)
		assert       g.edges["TEMPLIERS"] != null
		assert       g.edges["UNKNOWN"] == null
		assert       g.edges.size() == 9

		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"] != null
		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"].size() == 1
		assert       g.edges["TEMPLIERS"].d_arcs["MAIN_SITE"][0].route_id == "SB"

		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE_15"] != null
		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE_15"].size() == 1
		assert       g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE_15"][0].route_id == "SB"

		assertEquals g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE_15"][0].times[7].trip_id, "SBdown08"
		assertEquals g.edges["TEMPLIERS"].d_arcs["GREEN_SIDE_15"][0].times[7].departure_time, "14:07:00"
	}

	public void testFindRoutes() {
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutes("GREEN_SIDE_15")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 8
		assert r["SB"][0].times[0].departure_time.equals("11:30:00")
	}

	public void testFindRoutesWithTime() {
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.findRoutesAndTimes("GREEN_SIDE_15", "11:46:00")
		assert r["SB"] != null
		assert r["SB"].size() == 1
		assert r["SB"][0].destination_id.equals("TEMPLIERS")
		assert r["SB"][0].times.size() == 7
		assert r["SB"][0].times[0].departure_time.equals("11:50:00")

		r = g.findRoutesAndTimes("GREEN_SIDE_15", "12:06:00")
		assert r["SB"][0].times.size() == 6
		assert r["SB"][0].times[0].departure_time.equals("12:10:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE_15", "12:26:00")
		assert r["SB"][0].times.size() == 5
		assert r["SB"][0].times[0].departure_time.equals("12:30:00")

		r = g.findRoutesAndTimes("GREEN_SIDE_15", "12:46:00")
		assert r["SB"][0].times.size() == 4
		assert r["SB"][0].times[0].departure_time.equals("13:15:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE_15", "13:16:00")
		assert r["SB"][0].times.size() == 3
		assert r["SB"][0].times[0].departure_time.equals("13:35:00")
		
		r = g.findRoutesAndTimes("GREEN_SIDE_15", "13:36:00")
		assert r["SB"][0].times.size() == 2
		assert r["SB"][0].times[0].departure_time.equals("13:55:00")
		assert r["SB"][0].times[0].nextDay.equals(false)
		
		r = g.findRoutesAndTimes("GREEN_SIDE_15", "13:56:00")
		assert r["SB"][0].times.size() == 8
		assert r["SB"][0].times[0].departure_time.equals("11:30:00")
		assert r["SB"][0].times[0].nextDay.equals(true)
	}
	
	public void testGetPaths() {
		def reader = new CsvFeedReader(rootpath, sname)
		def feed = reader.read()
		Graph g = new Graph(feed)
		def r = g.getPaths()
		assert r != null
		assert r.size() == 6
	}
}
