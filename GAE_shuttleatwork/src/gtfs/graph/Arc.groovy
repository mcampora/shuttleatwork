package gtfs.graph

import gtfs.model.*

/**
 * One arc is connecting an edge with another one
 * and is associated to a given route, trip
 * a list of departure time is associated to the arc
 */
class Arc {
	//static final transient int MAX_RESULTS = 3;
	def route_id;
	String origin_id; // stop_id
	String destination_id; // stop_id
	
	//List<Time> times = new ArrayList<Time>()
	Set<Time> times = new TreeSet<Time>()
	
	def Arc(String route_id, String origin_id, String destination_id) {
		this.route_id = route_id
		this.origin_id = origin_id
		this.destination_id = destination_id
	}
	
	void addTime(String trip_id, String departure_time, String arrival_time) {
		Time t = new Time(trip_id, departure_time, arrival_time)
		times.add(t)
		//improve that
		//times.sort { it.departure }
	}
	
	Arc findTimes(def departure_time) {
		Arc arc = new Arc(route_id, origin_id, destination_id)
		def more = true;
		times.each { time ->
			//println "$route_id $time.departure"
			if (time.after(departure_time) && more) {
				arc.times.add(time)
				if (arc.times.size() == 3) more = false;
			}
		}
		if (arc.times.size() == 0) {
			times.each { time ->
				if (more) {
					def copy = time.clone()
					copy.nextDay = true
					arc.times.add(copy)
					if (arc.times.size() == 3) more = false;
				}
			}
		}
		return arc
	}
}
