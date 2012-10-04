package gtfs

import geo.*;

class Feed {
	def agency = new Agency();
	def routes = [:];
	def shapes = [:]
	def calendars = [:];
	def trips = [:];
	def stops = [:];
	Area bound = new Area();
	Geoset set = new Geoset();
	
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
	
	def addStop(def id, Stop stop) {
		stops[id] = stop;
		bound.extend(stop.getPos());
		set.add(stop.getPos(), stop);
	}
	
	def addTrip(def id, def trip) {
		trips[id] = trip;
	}
	
	def addCalendar(def id, def cal) {
		calendars[id] = cal;
	}
	
	def addShape(def id, Shape shape) {
		shapes[id] = shape;
		//bound.extend(area);
	}
	
	def addRoute(def id, def route) {
		routes[id] = route;
	}
	
	String toString() {
		return "[ agency:'$agency',\n  routes:'$routes',\n  shapes:'$shapes',\n  calendars:'$calendars',\n " +
		" trips:'$trips',\n  stops:'$stops',\n  area:'$area' ]";
	}
}
