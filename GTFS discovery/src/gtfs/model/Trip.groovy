package gtfs.model

/**
 * A trip represents the course of a given transport and the sequence of stops 
 * it will do
 * It is associated to a shape (the representation of its route),
 * A service id (ie. a link to a week table),
 * 
 */
class Trip {
	String route_id;
	String service_id;
	String trip_id;
	String trip_headsign;
	String direction_id;
	String block_id;
	String shape_id;
	List<StopTime> stoptimes = [];

	def addStopTime(StopTime st) {
		stoptimes.add(st);
	}


	String toString() {
		return "[ route:'${route.getRoute_id()}', service:'${service.getService_id()}', trip_id:'$trip_id', " +
		"trip_headsign:'$trip_headsign', direction_id:'$direction_id', block_id:'$block_id', " +
		"shape:'${shape.getShape_id()}', stoptimes:'$stoptimes' ]";
	}
}
