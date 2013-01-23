package gtfs.graph

class Arc {
	def departure; // stoptime
	def arrival; // stoptime
	def trip;

	def Arc(def trip) {
		this.trip = trip
	}
}

