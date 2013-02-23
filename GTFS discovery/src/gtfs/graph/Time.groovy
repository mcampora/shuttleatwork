package gtfs.graph

import java.text.*

class Time {
	static DateFormat fmt = new SimpleDateFormat("HH:mm:ss")
	String trip_id;
	String departure_time; // stoptime
	String arrival_time; // stoptime
	Date departure;

	def Time(def trip_id, def departure_time, def arrival_time) {
		this.trip_id = trip_id
		this.departure_time = departure_time
		this.arrival_time = arrival_time
		departure = fmt.parse(departure_time)
	}
	
	boolean after(def departure_time) {
		def current = fmt.parse(departure_time)
		return departure.after(current)
	}
}
