package shuttletowork.actions;

import static org.junit.Assert.*;
import shuttletowork.server.*;
import shuttletowork.system.*;

class ActionsTest extends GroovyTestCase {
	def execute(script, param) {
		def ss = new ScriptService("${Env.ROOT}/src/shuttletowork/actions/")
		return new String(ss.execute(script, param))
	}

	public void testFindItinerary() {
		//println execute("findItinerary", [origin_id: 'GREEN_SIDE', destination_id: 'MAIN_SITE'])
	}

	public void testFindRoutes() {
		def rr = execute("findRoutes", [stop_id:'GREEN_SIDE'])
		assert rr != null
		assert rr.startsWith("{ 'jsonid':1, 'SB':[ { 'jsonid':3, 'times':[ { 'jsonid':5, ");

		rr = execute("findRoutes", [stop_id:'TEMPLIERS'])
		assert rr != null
		assert rr.startsWith("{ 'jsonid':1, 'SB':[ { 'jsonid':3, 'times':[ { 'jsonid':5, ");

		rr = execute("findRoutes", [stop_id:'NAXOS'])
		assert rr != null
		assert rr.startsWith("{ 'jsonid':1, 'SA':[ { 'jsonid':3, 'times':[ { 'jsonid':5, ");
	}

	public void testGetArea() {
		assertEquals execute("getArea", null),
			"{ 'jsonid':1, 'max':{ 'jsonid':2, 'lat':43.626399, 'lon':7.074561,  }, 'min':{ 'jsonid':3, 'lat':43.616287, 'lon':7.041944,  },  }"
	}

	public void testGetStops() {
		assert execute("getStops", null).startsWith(
			"{ 'jsonid':1, 'GREEN_SIDE':{ 'jsonid':2, 'stop_id':'GREEN_SIDE'");
	}

	public void testHelloWorld() {
		assertEquals execute("helloWorld", null), "Hello world! null"
		assertEquals execute("helloWorld", [a:'x']), "Hello world! [a:x]"
	}
}
