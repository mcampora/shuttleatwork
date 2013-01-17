package gtfs.model

/**
 * A trip represents the course of a given transport and the sequence of stops 
 * it will do
 * It is associated to a shape (the representation of its route),
 * A service id (ie. a link to a week table),
 * 
 */
class Trip {
	def route_id;
	def route; // id
	def service_id;
	def service; // id
	def trip_id;
	def trip_headsign;
	def direction_id;
	def block_id;
	def shape_id;
	def shape; // id
	def stoptimes = [];

	def addStopTime(def st) {
		stoptimes.add(st);
	}


	String toString() {
		return "[ route:'${route.getRoute_id()}', service:'${service.getService_id()}', trip_id:'$trip_id', " +
		"trip_headsign:'$trip_headsign', direction_id:'$direction_id', block_id:'$block_id', " +
		"shape:'${shape.getShape_id()}', stoptimes:'$stoptimes' ]";
	}
}
