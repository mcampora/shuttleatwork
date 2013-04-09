package shuttleatwork.actions;

//import static org.junit.Assert.*;
import shuttleatwork.server.*;
import shuttleatwork.system.*;

class ActionsTest extends GroovyTestCase {
	def execute(script, param) {
		def ss = new ScriptService("")
		return new String(ss.execute(script, param))
	}

	public void testFindItinerary() {
		//println execute("findItinerary", [origin_id: \"GREEN_SIDE\", destination_id: \"MAIN_SITE\"])
	}

	public void testFindRoutes() {
		def rr = execute("findRoutes", [stop_id:['GREEN_SIDE_15']])
		assert rr != null
		assert rr.startsWith("{ \"SB\":[ { \"times\":[ { ");

		rr = execute("findRoutes", [stop_id:['TEMPLIERS']])
		assert rr != null
		assert rr.startsWith("{ \"SB\":[ { \"times\":[ { ");

		rr = execute("findRoutes", [stop_id:['NAXOS']])
		assert rr != null
		assert rr.startsWith("{ \"SA\":[ { \"times\":[ { ");
	}

	public void testFindRoutesAndNextDepartures() {
		def rr = execute("findRoutesAndNextDepartures", [stop_id:['GREEN_SIDE_15']])
		assert rr != null
		assert rr.startsWith("{ \"SB\":[ { \"times\":[ ");
		assert rr.contains("nextDay") == true
		println rr
	}

	public void testGetArea() {
		assertEquals execute("getArea", null),
			"{ \"max\":{ \"lat\":43.626399, \"lon\":7.075394 }, \"min\":{ \"lat\":43.616287, \"lon\":7.041944 } }"
	}
	
	public void testGetStops() {
		assert execute("getStops", null).startsWith(
			"{ \"GREEN_SIDE_15\":{ \"stop_id\":\"GREEN_SIDE_15\"");
	}

	public void testGetPaths() {
		assert execute("getPaths", null).startsWith(
			"[ [");
	}

	public void testHelloWorld() {
		assertEquals execute("helloWorld", null), "Hello world! null"
		assertEquals execute("helloWorld", [a:'x']), "Hello world! [a:x]"
	}
}
