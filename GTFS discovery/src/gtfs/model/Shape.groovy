package gtfs.model

import utils.geo.*;

class Shape {
	def shape_id;
	def shape_pts = [];
	Area bound = new Area();

	def addPoint(ShapePoint pt) {
		shape_pts.add(pt);
		bound.extend(pt.getPos())
	}

	String toString() {
		return "[ shape_id:'$shape_id', shape_pts:'$shape_pts', area:'$bound' ]";
	}
}
