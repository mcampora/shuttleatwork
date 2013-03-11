package gtfs.reader

import utils.geo.*;
import utils.csv.*;
import gtfs.model.*;

/**
 * Build a feed in memory based on a set of CSV files
 * need a path to find the files
 * translate some of the keys into direct references
 * TBD
 * 	1/ decouple the data source from the object creation (ie. data could come from the database as table, columns and rows)
 * 	2/ control inconsistencies errors and generate a report
 */
class CsvFeedReader {
	String path
	String name

	def CsvFeedReader(def path, def name) {
		this.path = "$path/$name/"
		this.name = name
	}

	Feed read() {
		Feed feed = new Feed()
		feed.name = name
		readAgency({ agency -> feed.setAgency(agency) })
		readRoutes({ route ->
			feed.addRoute(route.getRoute_id(), route);
		})
		readShapes({ pt ->
			Shape shape = feed.getShape(pt.getShape_id());
			if (shape == null) {
				shape = new Shape();
				shape.setShape_id(pt.getShape_id());
				feed.addShape(shape.getShape_id(), shape);
			}
			shape.addPoint(pt);
		})
		readCalendars({ cal -> feed.addCalendar(cal.getService_id(), cal) })
		readTrips({ trip ->
			feed.addTrip(trip.getTrip_id(), trip)
		})
		readStops({ stop ->
			feed.addStop(stop.getStop_id(), stop)
		})
		readStoptimes({ stoptime ->
			Trip t = feed.getTrip(stoptime.getTrip_id())
			t.addStopTime(stoptime);
		})
		return feed
	}

	def readFile(def name, def clazz, def closure) {
		def reader = new Reader(clazz, closure)
		return reader.translate(path + name)
	}

	def readAgency(closure) {
		return readFile("agency.txt", Agency.class, closure)[0]
	}

	def readRoutes(closure) {
		return readFile("routes.txt", Route.class, closure)
	}

	def readShapes(closure) {
		return readFile("shapes.txt", ShapePoint.class, { pt ->
				pt.setPos(new Position(pt.getShape_pt_lat(), pt.getShape_pt_lon()));
				closure(pt);
		})
	}

	def readCalendars(closure) {
		return readFile("calendar.txt", Calendar.class, closure)
	}

	def readTrips(closure) {
		return readFile("trips.txt", Trip.class, closure)
	}

	def readStops(closure) {
		return readFile("stops.txt", Stop.class, { stop ->
			stop.setPos(new Position(stop.getStop_lat(), stop.getStop_lon()));
			if (closure != null) closure(stop)
		})
	}

	def readStoptimes(closure) {
		return readFile("stop_times.txt", StopTime.class, closure)
	}
}
