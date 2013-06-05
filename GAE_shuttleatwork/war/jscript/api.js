var network = {
    DEFAULT_FEED: "1Ashuttle", 
    //DEFAULT_FEED: "bart-archiver_20120705_0313",
    
    trips: {},

    // error message in case of difficulties with the server
	serverIssue: function(status) {
		alert("Issue with the server or the returned string!");
		console.log(status.responseText); 
		$.parseJSON(status.responseText);
	},

	//get the geographical area containing the current feed(s)
	getArea: function(fn) {
		$.getJSON("/scriptlet?action=getArea&feed=" + network.DEFAULT_FEED, fn).error(this.serverIssue); 
	},

	//get the stops and their respective position
	getStops: function(fn) {
		$.getJSON("/scriptlet?action=getStops&feed=" + network.DEFAULT_FEED, function(obj) {
			stops = obj;
			if (fn != null) fn(stops);
		}).error(this.serverIssue); 	
	},
	
	// deprecated
	//get the trips
	getTrips: function(fn) {
		$.getJSON("/scriptlet?action=getTrips&feed=" + network.DEFAULT_FEED, function(obj){
			trips = obj;
			if (fn != null) fn(trips);
		});
	},

	//get the routes
	getRoutes: function(fn) {
		$.getJSON("/scriptlet?action=getRoutes&feed=" + network.DEFAULT_FEED, function(obj){
			routes = obj;
			if (fn != null) fn(routes);
		});
	},

	//get the shapes
	getShapes: function(fn) {
		$.getJSON("/scriptlet?action=getShapes&feed=" + network.DEFAULT_FEED, function(obj){
			shapes = obj;
			if (fn != null) fn(shapes);
		});
	},

	// find the routes starting from a given destination
	findRoutes: function(id, fn) {
		url = "/scriptlet?action=findRoutes&stop_id=" + id + "&feed=" + network.DEFAULT_FEED;
		$.getJSON(url, function(obj){
			if (fn != null)	fn(obj);
		});
	},

	// find the routes starting from a given destination
	// and the next departures considering current time
	findRoutesAndNextDepartures: function(id, fn) {
		url = "/scriptlet?action=findRoutesAndNextDepartures&stop_id=" + id + "&feed=" + network.DEFAULT_FEED;
		$.getJSON(url, function(obj, textStatus, jqxhr){
			//alert(jqxhr.responseText);
			if (fn != null)	fn(obj);
		});
	},
	
	//get the various paths
	getPaths: function(fn) {
		$.getJSON("/scriptlet?action=getPaths&feed=" + network.DEFAULT_FEED, function(obj) {
			paths = obj;
			if (fn != null) fn(paths);
		});
	}
}
