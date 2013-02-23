// error message in case of difficulties with the server
function serverIssue(status) {
	alert("Issue with the server!");
	console.log(status.responseText); 
	$.parseJSON(status.responseText);
}

//get the geographical area containing the current feed(s)
function getArea(fn) {
	$.getJSON("/script/getArea", fn).error(serverIssue); 
}

//get the stops and their respective position
function getStops(fn) {
	$.getJSON("/script/getStops", function(obj) {
		stops = obj;
		if (fn != null) fn(stops);
	}).error(serverIssue); 
	
}

//get the trips
function getTrips(fn) {
	$.getJSON("/script/getTrips", function(obj){
		trips = obj;
		if (fn != null) fn(trips);
	});
}

//get the routes
function getRoutes(fn) {
	$.getJSON("/script/getRoutes", function(obj){
		routes = obj;
		if (fn != null) fn(routes);
	});
}

//get the shapes
function getShapes(fn) {
	$.getJSON("/script/getShapes", function(obj){
		shapes = obj;
		if (fn != null) fn(shapes);
	});
}

// find the routes starting from a given destination
function findRoutes(id, fn) {
	url = "/script/findRoutes?stop_id=" + id;
	$.getJSON(url, function(obj){
		if (fn != null)	fn(obj);
	});
}

// find the routes starting from a given destination
// and the next departures considering current time
function findRoutesAndNextDepartures(id, fn) {
	url = "/script/findRoutes?stop_id=" + id;
	$.getJSON(url, function(obj){
		if (fn != null)	fn(obj);
	});
}

