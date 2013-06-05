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
	
	public void test2ndNetwork() {
		def fac = Facade.getInstance()
		def feed = fac.getFeed("bart-archiver_20120705_0313")
		assert feed != null
		assert feed.getAgency() != null
		assertEquals feed.getAgency().agency_name, "AirBART" // keep only the last record
		assert feed.getStops() != null
		assert feed.getStops().size() > 0
	}
}
