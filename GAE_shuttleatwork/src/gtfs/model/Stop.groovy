package gtfs.model

import utils.geo.*;

/**
 * A stop in the trip (ex. a bus station, a subway station, ...)
 */
class Stop {
	String stop_id;
	String stop_name;
	String stop_desc;
	String stop_lat;
	String stop_lon;
	String zone_id;
	String stop_url;
	
	Position pos;
	
	String toString() {
		return "[ stop_id:'$stop_id', stop_name:'$stop_name', stop_desc:'$stop_desc', " +
		"pos:'$pos', zone_id:'$zone_id', stop_url:'$stop_url' ]";
	}
}
