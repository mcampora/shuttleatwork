package gtfs.reader;

import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;
import gtfs.model.*;

class FeedReaderTest extends GroovyTestCase {
	def rootpath = "${System.properties['user.dir']}/rsc/1Ashuttle/"
	
	public void testAgency() {
		def reader = new CsvFeedReader(rootpath)
		def agency = reader.readAgency()		
		assertEquals agency.agency_id, "1A-SEP" 
		assertEquals agency.agency_name, "Amadeus Corporation (SEP contribution)" 
	}

	public void testRoutes() {
		def reader = new CsvFeedReader(rootpath)
		def routes = reader.readRoutes({ it.setAgency("agency"); })		
		assertEquals routes[0].agency, "agency" 
		assertEquals routes[0].route_id, "SA" 
		assertEquals routes[0].agency_id, "1A-SEP" 
	}
	
	public void testShapes() {
		Feed feed = new Feed()
		def reader = new CsvFeedReader(rootpath)
		def shape_points = reader.readShapes({ pt ->
			Shape shape = feed.getShape(pt.getShape_id());
			if (shape == null) {
				shape = new Shape();
				shape.setShape_id(pt.getShape_id());
				feed.addShape(shape.getShape_id(), shape);
			}
			shape.addPoint(pt);
		})
		assert feed.getShapes().size() == 4
		assert shape_points.size() == 324
		assertEquals shape_points[0].shape_id, "SB1"
	}
	
	public void testCalendar() {
		def reader = new CsvFeedReader(rootpath)
		def cals = reader.readCalendars()
		assert cals.size() == 1
		assertEquals cals[0].getService_id(), "WEEK"
	}
	
	public void testTrips() {
		def reader = new CsvFeedReader(rootpath)
		def trips = reader.readTrips({ trip -> 
			trip.setRoute(trip.getRoute_id());
			trip.setService(trip.getService_id());
		})
		assert trips.size() == 32	
		assertEquals trips[0].getRoute_id(), "SA"
		assertEquals trips[0].getRoute(), "SA"
		assertEquals trips[0].getService_id(), "WEEK"
		assertEquals trips[0].getService(), "WEEK"
	}
	
	public void testStops() {
		def reader = new CsvFeedReader(rootpath)
		def stops = reader.readStops()
		assert stops.size() == 6
		assertEquals stops[0].getStop_id(), "GREEN_SIDE"
	}
	
	public void testStoptimes() {
		def reader = new CsvFeedReader(rootpath)
		def stoptimes = reader.readStoptimes()
		assert stoptimes.size() == 112
		assertEquals stoptimes[0].getTrip_id(), "SAup01"
	}
	
	public void testFeedReader() {
		def reader = new CsvFeedReader(rootpath)
		def feed = reader.read()
		assertEquals feed.getAgency().getAgency_id(), "1A-SEP"
		assert feed.getStops() != null
		assert feed.getStops().size() > 0
	}
}
