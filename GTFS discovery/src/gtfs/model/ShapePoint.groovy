package gtfs.model

import utils.geo.*;

/**
 * A geographical point used to represent a route
 */
class ShapePoint {
	String shape_id;
	String shape_pt_lat;
	String shape_pt_lon;
	String shape_pt_sequence;
	String shape_dist_traveled;
	
	Position pos;

	String toString() {
		return "[ pos:'$pos', shape_pt_sequence:'$shape_pt_sequence', shape_dist_traveled:'$shape_dist_traveled' ]";
	}
}
