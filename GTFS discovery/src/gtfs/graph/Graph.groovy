package gtfs.graph

import facade.*

class Graph {
	def edges = [:]

	def Graph(def feed) {
		feed.stops.each { k, v ->
			edges[k] = new Edge(feed, v)
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
