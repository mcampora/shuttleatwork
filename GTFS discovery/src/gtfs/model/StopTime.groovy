package gtfs.model

/**
 * A time at which the transport mode reach a given stop in a given trip 
 */
class StopTime {
	def trip_id;
	def arrival_time;
	def departure_time;
	def stop_id;
	def stop_sequence;
	def stop_headsign;
	def pickup_type;
	def drop_off_type;
	def shape_dist_traveled;

	String toString() {
		return "[ trip:'$trip_id', arrival_time:'$arrival_time', departure_time:'$departure_time', " +
		"stop:'$stop_id', stop_sequence:'$stop_sequence', stop_headsign:'$stop_headsign', " +
		"pickup_type:'$pickup_type',  drop_off_type:'$drop_off_type', shape_dist_traveled:'$shape_dist_traveled' ]";
	}
}
