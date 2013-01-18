import junit.framework.Test;

import junit.framework.TestSuite;
import groovy.util.GroovyTestSuite;

public class AllTests extends TestSuite {

	@SuppressWarnings("unchecked")
    public static Test suite() throws Exception {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		GroovyTestSuite gsuite = new GroovyTestSuite();
        suite.addTestSuite(gsuite.compile("test/shuttletowork/actions/ActionsTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/shuttletowork/system/JSonTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/shuttletowork/server/FileServiceTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/shuttletowork/server/ScriptServiceTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/shuttletowork/model/FacadeTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/gtfs/reader/FeedReaderTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/utils/csv/ReaderTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/utils/geo/PositionTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/utils/geo/AreaTest.groovy"));
        suite.addTestSuite(gsuite.compile("test/utils/geo/GeosetTest.groovy"));
        //$JUnit-END$
		return suite;
	}

}
