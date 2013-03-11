package shuttleatwork.server;

//import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;
import shuttleatwork.server.ScriptService;
import shuttletowork.system.*


class ScriptServiceTest extends GroovyTestCase {
	
	def execute(script, param) {
		def ss = new ScriptService("")
		return new String(ss.execute(script, param))
	}
	
	public void testHelloWorld() {
		assertEquals execute("helloWorld", null), "Hello world! null"
		assertEquals execute("helloWorld", [a:'x']), "Hello world! [a:x]"
	}
}
