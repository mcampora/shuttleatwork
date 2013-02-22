package gtfs.graph

import gtfs.model.*

/**
 * An edge is a stop and for each route passing through this stop maintains a list of arcs connecting to another edge
 * Each arc is associated to a given route, trip and stoptime (a destination and departure time)
 */
class Edge {
	String stop_id

	// the list of arcs going through this stop indexed by route id and destination id
	Map<String,Arc> arcs = [:]

	// indexed by route id
	Map<String,List<Arc>> r_arcs = [:]
	// indexed by destination id
	Map<String,List<Arc>> d_arcs = [:]

	def Edge(String stop_id) {
		this.stop_id = stop_id
	}

	def addArc(String route_id, String trip_id, String departure_time, String destination_id, String arrival_time) {
		Arc arc = arcs["$route_id-$destination_id"]
		List<Arc> r_arc = r_arcs[route_id]
		List<Arc> d_arc = d_arcs[destination_id]

		if (r_arc == null) {
			r_arc = []
			r_arcs[route_id] = r_arc
		}

		if (d_arc == null) {
			d_arc = []
			d_arcs[destination_id] = d_arc
		}

		if (arc == null) {
			arc = new Arc(route_id, stop_id, destination_id)
			arcs["$route_id-$destination_id"] = arc
			r_arc.add(arc)
			d_arc.add(arc)
			//println "$stop_id - $route_id - $stop_id -> $destination_id"
		}

		arc.addTime(trip_id, departure_time, arrival_time)
	}

	public String toString() {
		return "Edge:${stop_id}"
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
