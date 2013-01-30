package gtfs.graph

import gtfs.model.*

/**
 * One arc is connecting an edge with another one
 * and is associated to a given route and trip
 */
class Arc {
	def route_id;
	String origin_id; // stop_id
	String destination_id; // stop_id
	
	List<Time> times = []
	
	def Arc(String route_id, String origin_id, String destination_id) {
		this.route_id = route_id
		this.origin_id = origin_id
		this.destination_id = destination_id
	}
	
	def addTime(String trip_id, String departure_time, String arrival_time) {
		Time t = new Time()
		t.trip_id = trip_id
		t.departure_time = departure_time
		t.arrival_time = arrival_time
		times.add(t)
	}
	
	public String toString() {
		return "Arc:${origin_id}->${destination_id}"
	}
}
