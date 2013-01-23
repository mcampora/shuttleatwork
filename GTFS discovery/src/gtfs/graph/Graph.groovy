package gtfs.graph

import facade.*
import gtfs.model.*

class Graph {
	def edges = [:]

	def Graph(Feed feed) {
		feed.stops.each { k, v ->
			edges[k] = new Edge(feed, v)
		}
		
		Map departures = [:]
		feed.trips.each { String k, Trip t ->
			println "$t.route_id / $t.trip_id:"
			t.stoptimes.each { StopTime st ->
				println "$st"
			}
		}
	}

	def findRoutes(def start) {
		return edges[start]
	}

	def findRoute(def start, def end) {
		// find a complete path to the destination
		return edges[start].findRoute(edges, [], end)
	}
}
