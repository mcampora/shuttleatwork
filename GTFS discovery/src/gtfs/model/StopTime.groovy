package gtfs.model

/**
 * A time at which the transport mode reach a given stop in a given trip 
 */
class StopTime {
	String trip_id;
	String arrival_time;
	String departure_time;
	String stop_id;
	String stop_sequence;
	String stop_headsign;
	String pickup_type;
	String drop_off_type;
	String shape_dist_traveled;

	String toString() {
		return "[ trip:'$trip_id', arrival_time:'$arrival_time', departure_time:'$departure_time', " +
		"stop:'$stop_id', stop_sequence:'$stop_sequence', stop_headsign:'$stop_headsign', " +
		"pickup_type:'$pickup_type',  drop_off_type:'$drop_off_type', shape_dist_traveled:'$shape_dist_traveled' ]";
	}
}
