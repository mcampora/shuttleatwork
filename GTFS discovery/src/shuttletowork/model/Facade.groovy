package shuttletowork.model

import gtfs.reader.*
import gtfs.graph.*
import shuttletowork.system.Env

class Facade {
	static def PATH = "${Env.ROOT}/rsc"
	static def DEFAULT_FEED = "1Ashuttle"

	def feed;
	def graph;

	def Facade() {
		def reader = new CsvFeedReader(PATH, DEFAULT_FEED)
		feed = reader.read()
		graph = new Graph(feed)
	}

	static def instance = null
	static synchronized def getInstance() {
		if (instance == null)
			instance = 	new Facade()
		return instance
	}

	List<String> getNetworks() {
		List<String> res = []
		res.add("A")
		res.add("B")
		return res;
	}
}
