package gtfs.graph

import java.util.Map

import facade.*
import gtfs.model.*

/**
 * Transform a GTFS model into an oriented graph with edges and arcs
 * An edge is a stop and maintains a list of arcs, one for each route passing through this stop
 * One arc is connecting this edge to another edge
 * Each arc maintains the list of departure time (ie. a stoptime)
 */
class Graph {
  Map<String,Edge> edges = [:] // stop_id -> Edge
  Feed feed;

  def Graph(Feed feed) {
	this.feed = feed
	
    // create one edge for each stop
    feed.stops.each { stop_id, stop ->
      edges[stop_id] = new Edge(stop_id)
    }
    // a bus will follow a given route several times during
    // a day that's what is called a trip
    // take trips one by one
    feed.trips.each { trip_id, trip ->
      // take each arrival to a given stop one by one
      Edge from = null
      StopTime prev = null
      trip.getStoptimes().each { stoptime ->
        Edge to = edges[stoptime.stop_id]
        if (from != null) {
          from.addArc(trip.route_id, trip.trip_id, prev.departure_time, stoptime.stop_id, stoptime.arrival_time)
        }
        from = to
        prev = stoptime
      }
    }
  }

  // helper function used to identify the number of shapes the
  // network should include to illustrate the various paths
  def getPaths() {
	def paths = []
    // get trips one by one
	feed.trips.each { trip_id, trip ->
		// build the path corresponding to this trip
		def path = []
		trip.stoptimes.each { stoptime ->
			path.add(stoptime.stop_id)
		}
		// if the path is new add it to the list
		if (!paths.contains(path))
			paths.add(path)
	}
	//println paths
	return paths
  }

  def findRoutes(def start) {
    return edges[start].r_arcs
  }

  def findRoutesAndTimes(def start, def departure_time) {
    return edges[start].findArcsAndTimes(departure_time)
  }

  def findRoute(def start, def end) {
    // find a complete path to the destination
    return edges[start].findRoute(edges, [], end)
  }
}
