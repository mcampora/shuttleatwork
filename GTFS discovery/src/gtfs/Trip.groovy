package gtfs

class Trip {
	def route; // id
	def service; // id
	def trip_id;
	def trip_headsign;
	def direction_id;
	def block_id;
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
