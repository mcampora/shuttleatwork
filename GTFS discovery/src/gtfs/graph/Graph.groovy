package gtfs.graph

import facade.*

class Graph {
	def feed;
	def edges = [:]

	static class Arc {
		def origin; // stoptime
		def destination; // stoptime
		def shape;
	}

	static class Edge {
		def stop_id
		def arcs = [:] // stop_id:[arc]

		String toString() {
			return "{ $stop_id, $arcs }"
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

	def Graph(def feed) {
		this.feed = feed;
		feed.stops.each { k, v ->
			edges[k] = buildArcs(k)
		}
	}

	def buildArcs(def start_id) {
		def edge = new Edge()
		edge.stop_id = start_id
		feed.getTrips().each { trip_id, trip ->
			def capture = false
			def arc = new Arc()
			trip.getStoptimes().each { stoptime ->
				if (capture == true) {
					def array = edge.arcs[stoptime.getStop_id()]
					if (array == null) {
						array = []
						edge.arcs[stoptime.getStop_id()] = array
					}
					arc.destination = stoptime
					arc.shape = trip.shape
					array.add(arc)
					arc = new Arc()
					capture = false
				}
				else if (stoptime.getStop_id().equals(start_id)) {
					arc.origin = stoptime
					capture = true
				}
			}
		}
		return edge
	}

	def findRoutes(def start) {
		return edges[start]
	}

	def findRoute(def start, def end) {
		// find a complete path to the destination
		return edges[start].findRoute(edges, [], end)
	}

	public static void main(String[] args) {
		def f = Facade.getInstance().feed
		Graph g = new Graph(f)

//		def fn = { n ->
//			println n.stop_id
//			n.arcs.each { k, v ->
//				println "  $k"
//				v.each { a ->
//					println "    -->"
//					println "      ${a.origin}"
//					println "      ${a.destination}"
//					println "      ${a.shape}"
//				}
//			}
//			println "----"
//		}
//
//		fn(g.findRoutes("GREEN_SIDE"))
//		fn(g.findRoutes("TEMPLIERS"))
//		fn(g.findRoutes("MAIN_SITE"))

		println g.findRoute("GREEN_SIDE", "MAIN_SITE")
	}
}
