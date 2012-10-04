package gtfs

import geo.*;

class FeedReader {
	String path
	Feed feed
	
	Feed read(String path) {
		this.path = path
		feed = new Feed()
		readAgency()
		readRoutes()
		readShapes()
		readCalendars()
		readTrips()
		readStops()
		readStoptimes()
		return feed
	}
	
	def readFile(String name, def closure) {
		def res
		def head = true
		new File(path + name).eachLine { line ->
			if (head) {
				head = false
			}
			else {
				def data = line.split(",")
				res = closure(data)
			}
		}
	}
	
	def readAgency() {
		readFile("agency.txt") {
			data ->
			Agency agency = feed.getAgency()
			agency.setAgency_id(data[0])
			agency.setAgency_name(data[1])
			agency.setAgency_url(data[2])
			agency.setAgency_timezone(data[3])
		}
	}
	
	def readRoutes() {
		readFile("routes.txt") {
			data ->
			Route route = new Route();
			route.setRoute_id(data[0]);
			// check here that we have the same id
			route.setAgency(feed.getAgency());
			route.setRoute_short_name(data[2]);
			route.setRoute_long_name(data[3]);
			route.setRoute_desc(data[4]);
			route.setRoute_type(data[5]);
			route.setRoute_url(data[6]);
			route.setRoute_color((data.length > 7)?(data[7]):null);
			route.setRoute_text_color((data.length > 8)?(data[8]):null);
			feed.addRoute(route.getRoute_id(), route);
		}
	}
	
	def readShapes() {
		readFile("shapes.txt") {
			data ->
			Shape shape = feed.getShape(data[0]);
			if (shape == null) {
				shape = new Shape();
				shape.setShape_id(data[0]);
				feed.addShape(data[0], shape);
			}
			ShapePoint pt = new ShapePoint()
			pt.setPos(new Position(data[1], data[2]));
			pt.setShape_pt_sequence(data[3]);
			if (data.length > 4)
				pt.setShape_dist_traveled(data[4]);
			shape.addPoint(pt);
		}
	}
	
	def readCalendars() {
		readFile("calendar.txt") {
			data ->
			Calendar cal = new Calendar();
			cal.setService_id(data[0]);
			cal.setMonday(data[1]);
			cal.setTuesday(data[2]);
			cal.setWednesday(data[3]);
			cal.setThursday(data[4]);
			cal.setFriday(data[5]);
			cal.setSaturday(data[6]);
			cal.setSunday(data[7]);
			cal.setStart_date(data[8]);
			cal.setEnd_date(data[9]);
			feed.addCalendar(data[0], cal);
		}
	}
	
	def readTrips() {
		readFile("trips.txt") {
			data ->
			Trip trip = new Trip();
			trip.setRoute(feed.getRoute(data[0]));
			trip.setService(feed.getCalendar(data[1]));
			trip.setTrip_id(data[2]);
			trip.setTrip_headsign(data[3]);
			trip.setDirection_id(data[4]);
			trip.setBlock_id((data.length > 5)?(data[5]):null);
			trip.setShape((data.length > 6)?feed.getShape(data[6]):null);
			feed.addTrip(data[2], trip);
		}
	}
	
	def readStops() {
		readFile("stops.txt") {
			data ->
			Stop stop = new Stop();
			stop.setStop_id(data[0]);
			stop.setStop_name(data[1]);
			stop.setStop_desc(data[2]);
			stop.setPos(new Position(data[3], data[4]));
			if (data.length > 5)
				stop.setZone_id(data[5]);
			if (data.length > 6)
				stop.setStop_url(data[6]);
			feed.addStop(data[0], stop);
		}
	}
	
	def readStoptimes() {
		readFile("stop_times.txt") {
			data ->
			StopTime stoptime = new StopTime();
			Trip t = feed.getTrip(data[0])
			stoptime.setTrip_id(data[0]);
			stoptime.setArrival_time(data[1]);
			stoptime.setDeparture_time(data[2]);
			stoptime.setStop_id(data[3]);
			stoptime.setStop_sequence(data[4]);
			if (data.length > 5)
				stoptime.setStop_headsign(data[5]);
			if (data.length > 6)
				stoptime.setPickup_type(data[6]);
			if (data.length > 7)
				stoptime.setDrop_off_type(data[7]);
			if (data.length > 8)
				stoptime.setShape_dist_traveled(data[8]);
			t.addStopTime(stoptime);
		}
	}
	
}
