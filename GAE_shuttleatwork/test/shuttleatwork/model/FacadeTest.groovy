package shuttleatwork.model;

//import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;

class FacadeTest extends GroovyTestCase {
	public void testInstance() {
		def fac = Facade.getInstance()
		def feed = fac.getFeed()
		assert feed != null
		assert feed.getAgency() != null
		assert feed.getStops() != null
		assert feed.getStops().size() > 0
	}
}
