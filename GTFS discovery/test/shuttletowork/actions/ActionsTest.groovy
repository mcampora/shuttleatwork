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
		assert rr.startsWith("{ 'jsonid':1, 'stop_id':'GREEN_SIDE', 'd_arcs':{ 'jsonid':2, 'TEMPLIERS'")
		
		rr = execute("findRoutes", [stop_id:'TEMPLIERS'])
		assert rr != null
		assert rr.startsWith("{ 'jsonid':1, 'stop_id':'TEMPLIERS', 'd_arcs':{ 'jsonid':2, 'MAIN_SITE'")

		rr = execute("findRoutes", [stop_id:'NAXOS'])
		assert rr != null
		assert rr.startsWith("{ 'jsonid':1, 'stop_id':'NAXOS', 'd_arcs':{ 'jsonid':2, 'TAISSOUNIERES'")
	}
	
	public void testGetArea() {
		assertEquals execute("getArea", null), 
			"{ 'jsonid':1, 'max':{ 'jsonid':2, 'lat':43.626399, 'lon':7.074561,  }, 'min':{ 'jsonid':3, 'lat':43.616287, 'lon':7.041944,  },  }"
	}
	
	public void testGetStops() {
		assertEquals execute("getStops", null),
			"[ { 'jsonid':2, 'stop_id':'GREEN_SIDE', 'stop_name':'Green Side', 'stop_lon':'7.071406', 'pos':{ 'jsonid':3, 'lat':43.62299, 'lon':7.071406,  }, 'stop_desc':'', 'stop_lat':'43.622990',  }, { 'jsonid':4, 'stop_id':'TEMPLIERS', 'stop_name':'Les Templiers (HP)', 'stop_lon':'7.074561', 'pos':{ 'jsonid':5, 'lat':43.616287, 'lon':7.074561,  }, 'stop_desc':'', 'stop_lat':'43.616287',  }, { 'jsonid':6, 'stop_id':'MAIN_SITE', 'stop_name':'Main site', 'stop_lon':'7.061976', 'pos':{ 'jsonid':7, 'lat':43.622998, 'lon':7.061976,  }, 'stop_desc':'', 'stop_lat':'43.622998',  }, { 'jsonid':8, 'stop_id':'TAISSOUNIERES', 'stop_name':'Les Taissounieres', 'stop_lon':'7.045378', 'pos':{ 'jsonid':9, 'lat':43.624908, 'lon':7.045378,  }, 'stop_desc':'', 'stop_lat':'43.624908',  }, { 'jsonid':10, 'stop_id':'CLARA', 'stop_name':'Clara', 'stop_lon':'7.052674', 'pos':{ 'jsonid':11, 'lat':43.626399, 'lon':7.052674,  }, 'stop_desc':'', 'stop_lat':'43.626399',  }, { 'jsonid':12, 'stop_id':'NAXOS', 'stop_name':'Naxos', 'stop_lon':'7.041944', 'pos':{ 'jsonid':13, 'lat':43.626289, 'lon':7.041944,  }, 'stop_desc':'', 'stop_lat':'43.626289',  },  ]"
	}
	
	public void testHelloWorld() {
		assertEquals execute("helloWorld", null), "Hello world! null"
		assertEquals execute("helloWorld", [a:'x']), "Hello world! [a:x]"
	}
}
