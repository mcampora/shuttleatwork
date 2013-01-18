package shuttletowork.server;

import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;
import shuttletowork.system.*

class FileServiceTest extends GroovyTestCase {

	def execute(script, param) {
		def ss = new FileService("${Env.ROOT}/home/")
		return ss.execute(script, param)
	}
	
	public void testGetHomePage() {
		def ss = new FileService("${Env.ROOT}/home/")
		def home = new String(ss.execute("index.html", null))
		assert home.contains("<head>")
	}
}
