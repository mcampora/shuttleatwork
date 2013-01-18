package shuttletowork.actions;

import static org.junit.Assert.*;
import shuttletowork.server.*;
import shuttletowork.system.*;

class ActionsTest extends GroovyTestCase {
	def execute(script, param) {
		def ss = new ScriptService("${Env.ROOT}/src/shuttletowork/actions/")
		return new String(ss.execute(script, param))
	}
	
	public void testFindRoute() {
		println execute("findRoute", [origin_id: 'GREEN_SIDE', destination_id: 'MAIN_SITE'])
	}
	
	public void testFindRoutes() {
		println execute("findRoutes", [stop_id:'GREEN_SIDE'])
	}
	
	public void testGetArea() {
		assertEquals execute("getArea", null), 
			"{ 'jsonid':1, 'max':{ 'jsonid':2, 'lat':-180.0, 'lon':-180.0,  }, 'min':{ 'jsonid':3, 'lat':180.0, 'lon':180.0,  },  }"
	}
	
	public void testGetStops() {
		assertEquals execute("getStops", null),
			"[" +
			"[ stop_id:'GREEN_SIDE', stop_name:'Green Side', stop_desc:'', pos:'[ lat:'43.62299', lon:'7.071406' ]', zone_id:'null', stop_url:'null' ], " +
			"[ stop_id:'TEMPLIERS', stop_name:'Les Templiers (HP)', stop_desc:'', pos:'[ lat:'43.616287', lon:'7.074561' ]', zone_id:'null', stop_url:'null' ], " +
			"[ stop_id:'MAIN_SITE', stop_name:'Main site', stop_desc:'', pos:'[ lat:'43.622998', lon:'7.061976' ]', zone_id:'null', stop_url:'null' ], " +
			"[ stop_id:'TAISSOUNIERES', stop_name:'Les Taissounieres', stop_desc:'', pos:'[ lat:'43.624908', lon:'7.045378' ]', zone_id:'null', stop_url:'null' ], " +
			"[ stop_id:'CLARA', stop_name:'Clara', stop_desc:'', pos:'[ lat:'43.626399', lon:'7.052674' ]', zone_id:'null', stop_url:'null' ], " +
			"[ stop_id:'NAXOS', stop_name:'Naxos', stop_desc:'', pos:'[ lat:'43.626289', lon:'7.041944' ]', zone_id:'null', stop_url:'null' ]]"
	}
	
	public void testHelloWorld() {
		assertEquals execute("helloWorld", null), "Hello world! null"
		assertEquals execute("helloWorld", [a:'x']), "Hello world! [a:x]"
	}
}
