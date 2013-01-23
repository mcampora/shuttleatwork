package gtfs.graph

class Arc {
	def departure; // stoptime
	def arrival; // stoptime
	def trip;
	def shape;

	def Arc(def trip, def shape) {
		this.trip = trip
		this.shape = shape
	}
}

