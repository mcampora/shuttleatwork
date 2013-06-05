package gtfs.model

import java.util.List;

import utils.geo.*;

/**
 * Entry point of the structure
 * Check https://developers.google.com/transit/gtfs/reference for more details
 */
class Feed {
	String name;
	Agency agency = new Agency();
	Map<String,Route> routes = [:];
	Map<String,Shape> shapes = [:]
	Map<String,Calendar> calendars = [:];
	Map<String,Trip> trips = [:];
	Map<String,Stop> stops = [:];
	Area bound = new Area();
	Geoset set = new Geoset();
	Map<String,List<StopTime>> stoptimes = [:];
	
	def getArea() {
		return bound
	}

	def getStop(def id) {
		return stops[id];
	}

	def getTrip(def id) {
		return trips[id];
	}

	def getCalendar(def id) {
		return calendars[id];
	}

	def getRoute(def id) {
		return routes[id];
	}

	def getShape(def id) {
		return shapes[id];
	}
	
	def getStoptimes(def trip_id) {
		return stoptimes[trip_id];
	}

	def addStop(Stop stop) {
		stops[stop.getStop_id()] = stop;
		bound.extend(stop.getPos());
		set.add(stop.getPos(), stop);
	}

	def addTrip(Trip trip) {
		trips[trip.getTrip_id()] = trip;
	}

	def addCalendar(Calendar cal) {
		calendars[cal.getService_id()] = cal;
	}

	def addShape(Shape shape) {
		shapes[shape.getShape_id()] = shape;
		//bound.extend(area);
	}

	def addRoute(Route route) {
		routes[route.getRoute_id()] = route;
	}
	
	def addStoptime(StopTime stoptime) {
		def ts = stoptimes[stoptime.getTrip_id()];
		if (ts == null) {
			stoptimes[stoptime.getTrip_id()] = ts = new ArrayList<StopTime>();
		}
		ts.add(stoptime);
	}

	String toString() {
		return "[ agency:'$agency',\n  routes:'$routes',\n  shapes:'$shapes',\n  calendars:'$calendars',\n " +
		" trips:'$trips',\n  stops:'$stops',\n  area:'$area' ]";
	}
}
