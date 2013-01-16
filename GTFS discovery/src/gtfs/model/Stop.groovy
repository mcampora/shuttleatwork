package gtfs.model

import utils.geo.*;

/**
 * A stop in the trip (ex. a bus station, a subway station, ...)
 */
class Stop {
	def stop_id;
	def stop_name;
	def stop_desc;
	Position pos;
	def zone_id;
	def stop_url;

	String toString() {
		return "[ stop_id:'$stop_id', stop_name:'$stop_name', stop_desc:'$stop_desc', " +
		"pos:'$pos', zone_id:'$zone_id', stop_url:'$stop_url' ]";
	}
}
