package shuttleatwork.model

import gtfs.reader.*
import gtfs.graph.*
import gtfs.model.*
import shuttleatwork.system.Env

class Facade {
	static def PATH = "rsc"
	static def DEFAULT_FEED = "1Ashuttle"

	private Map<String, Feed> feeds = new HashMap<String, Feed>();
	private Map<String, Graph> graphs = new HashMap<String, Graph>();

	def Facade() {
	}

	static def instance = null
	static synchronized def getInstance() {
		if (instance == null)
			instance = 	new Facade()
		return instance
	}
	
	void loadFeed(String name) {
		def reader = new CsvFeedReader(PATH, name)
		feeds[name] = reader.read()
		graphs[name] = new Graph(feeds[name])
	}
	
	Graph getGraph(String name = DEFAULT_FEED) {
		if (name == null) name = DEFAULT_FEED
		def graph = graphs[name]
		if (graph == null) {
			loadFeed(name)
			graph = graphs[name]
		}
		return graph
	}

	Feed getFeed(String name = DEFAULT_FEED) {
		if (name == null) name = DEFAULT_FEED
		def feed = feeds[name]
		if (feed == null) {
			loadFeed(name)
			feed = feeds[name]
		}
		return feed
	}
}
