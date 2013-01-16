package gtfs.model

import utils.geo.*;

/**
 * A geographical point used to represent a route
 */
class ShapePoint {
	def shape_pt_sequence;
	def shape_dist_traveled;
	Position pos;

	String toString() {
		return "[ pos:'$pos', shape_pt_sequence:'$shape_pt_sequence', shape_dist_traveled:'$shape_dist_traveled' ]";
	}
}
