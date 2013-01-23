package gtfs.graph

class Edge {
	def stop
	def arcs = [:] // stop_id:[arc]

	def Edge(def feed, def stop) {
		this.stop = stop
		feed.getTrips().each { trip_id, trip ->
			def capture = false
			def arc = new Arc(trip)
			trip.getStoptimes().each { stoptime ->
				if (capture == true) {
					arc.arrival = stoptime
					def array = arcs[stoptime.getStop_id()]
					if (array == null) {
						array = []
						arcs[stoptime.getStop_id()] = array
					}
					array.add(arc)
					capture = false
					arc = new Arc(trip)
				}
				else if (stoptime.getStop_id().equals(stop.stop_id)) {
					arc.departure = stoptime
					capture = true
				}
			}
		}
	}

	String toString() {
		return "{ $stop, $arcs }"
	}

	def findRoute(def edges, def stop_list, def destination_id) {
		println "${stop_list.size()}"
		if (stop_id.equals(destination_id))
			return stop_list
		arcs.each{ k, earcs ->
			def arc = earcs[0]
			def nextstop_id = arc.destination.stop_id
			def nextedge = edges[nextstop_id]
			return nextedge.findRoute(edges, stop_list + this, destination_id)
		}
		return null
	}
}
