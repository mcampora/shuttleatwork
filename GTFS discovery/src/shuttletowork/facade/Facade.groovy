package shuttletowork.facade

import utils.geo.*;
import gtfs.reader.*
import shuttletowork.graph.*;

class Facade {
	static def wROOT='D:/Documents'
	static def xROOT='/Users/Marc'
	static def ROOT="${xROOT}/Google Drive/GTFS discovery"

	static def FEED = "1Ashuttle"
	//static def FEED = "bart-archiver_20120705_0313"

	static def PATH = "${ROOT}/rsc/$FEED/"

	def reader;
	def feed;
	def graph;

	def Facade() {
		reader = new CsvFeedReader()
		feed = reader.read(PATH)
		graph = new Graph(feed)
	}

	static def instance = null
	static def getInstance() {
		if (instance == null)
			instance = 	new Facade()
		return instance
	}

}
