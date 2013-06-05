package gtfs.graph

import java.text.*

class Time implements Comparable 
{
	static DateFormat fmt = new SimpleDateFormat("HH:mm:ss")
	String trip_id;
	String departure_time; // stoptime
	String arrival_time; // stoptime
	Date departure;
	boolean nextDay = false;

	def Time(def trip_id, def departure_time, def arrival_time) {
		this.trip_id = trip_id
		this.departure_time = departure_time
		this.arrival_time = arrival_time
		departure = fmt.parse(departure_time)
	}
	
	boolean after(String departure_time) {
		def current = new Time("", departure_time, departure_time)
		return this.after(current)
	}
	
	boolean after(Time current) {
		return departure.after(current.departure)
	}
	
	def clone() {
		return new Time(trip_id, departure_time, arrival_time)
	}

	@Override
	public int compareTo(Object arg0) {
		return (departure.compareTo(arg0.departure));
	}
}
