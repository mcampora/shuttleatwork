package gtfs.model

import utils.geo.*;

/**
 * A stop in the trip (ex. a bus station, a subway station, ...)
 */
class Stop {
	def stop_id;
	def stop_name;
	def stop_desc;
	def stop_lat;
	def stop_lon;
	def zone_id;
	def stop_url;
	
	Position pos;
	
	String toString() {
		return "[ stop_id:'$stop_id', stop_name:'$stop_name', stop_desc:'$stop_desc', " +
		"pos:'$pos', zone_id:'$zone_id', stop_url:'$stop_url' ]";
	}
}
