package shuttletowork.model

import gtfs.reader.*
import gtfs.graph.*
import shuttletowork.system.Env

class Facade {
	static def FEED = "1Ashuttle"
	static def PATH = "${Env.ROOT}/rsc/$FEED/"

	def feed;
	def graph;

	def Facade() {
		def reader = new CsvFeedReader(PATH)
		feed = reader.read()
		graph = new Graph(feed)
	}

	static def instance = null
	static synchronized def getInstance() {
		if (instance == null)
			instance = 	new Facade()
		return instance
	}
}
