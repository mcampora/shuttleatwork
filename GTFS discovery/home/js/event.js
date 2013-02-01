// keep track of the user selections
var origin = null; // first selection
var destination = null; // second selection

// if not null then the info window is displayed with its content
var info = null; // should be non null after the first selection

// list of routes
var hshapes = null; // should be non null after the first selection

// fired when the map viewpoint is changed or there's
// a need to refresh markers, routes and details
function refreshMap() {
	console.log('refreshMap');
	if (stops != null) {
		map.clearOverlays();
	for (var stop_id in stops) {
		if (stop_id != "jsonid") {
			var stop = stops[stop_id];
  			var marker = addStopMarker(stop);
  			stop.marker = marker;
		}
  	}
	if (hshapes != null)
		displayShapes();
	if (info != null)
		displayDetails(origin.marker, info);
  }
}
// add a marker on the map corresponding to a given stop
function addStopMarker(stop) {
    var icon;
    if (stop == origin) {
      icon = iconOrigin;
    }
    else if (stop == destination) {
    	icon = iconDestination;
    }
    else {
      icon = iconBackground;
    }
    var ll = new GLatLng(stop.pos.lat, stop.pos.lon);
    var marker;
    marker = new GMarker(ll, {icon: icon, draggable: false});
    marker.stop = stop;
    stop.marker = marker;
    map.addOverlay(marker);
    marker.clickListener = GEvent.addListener(marker, "click", function() {onStopSelect(marker);});
    return marker;
}
function displayShapes() {
	console.log('displayShapes');
	// retrieve the shapes corresponding to the first departure to a given destination (so 1 shape per destination)
	var shapes = [];
	if (shapes != null) {
		for (var p in routes.arcs) {
			var pval = shapes.arcs[p][0];
			if (pval != null) {
				shapes[shapes.length] = pval.shape;
			}
		}
	}
	//console.log(shapes);
	for (var j=0; j<shapes.length; j++) {
		var shape = shapes[j];
		//console.log(shape);
		var linePoints = Array();
		for (var i = 0; i < shape.shape_pts.length; i++) {
			var ll = new GLatLng(shape.shape_pts[i].pos.lat, shape.shape_pts[i].pos.lon);
			linePoints[linePoints.length] = ll;
		}
		var color; // = shape.route.route_color;
		var polyline = new GPolyline(linePoints, color, 4);
		map.addOverlay(polyline);
  	}
}
function displayDetails(marker, info) {
	console.log('displayDetails');
    var html = "<div class='stopinfo'>";

    // name of the stop
	html = html + "<p class='stopname'>" + marker.stop.stop_name + "</p>";

    for (var route_id in info) {
    	if (route_id != "jsonid") {
    		var route = routes[route_id];

    		// name of the route
    		html = html + "<span class='route' style='background-color:#" + route.route_color +
    			";color:#" + route.route_text_color + ";'>" +
    			route.route_long_name +
    			" (" + route.route_short_name +	")</span>";

    		html = html + "<ul class='tostop'>";
    		var arcs = info[route_id];
    		for (var i=0,len=arcs.length; i<len; i++) {
    			var arc = arcs[i];
    			var destination = stops[arc.destination_id];

    			// name of the next stop
    			html = html + "<li>" + destination.stop_name + "</li>";

    			html = html + "<ul class='departuretime'>";
	    		for (var j=0,jlen=arcs[i].times.length; (j<jlen && j<3); j++) {
	    			var trip = trips[arcs[i].times[j].trip_id]
    				html = html + "<li>" + trip.trip_headsign + " - " + arcs[i].times[j].departure_time + "</li>";
	    		}
	    		html = html + "</ul>";
    		}
    		html = html + "</ul>";
    	}
    }
    html = html + "</div></div>";
    origin.marker.openInfoWindowHtml(html);
}

// fired when the user select a stop on the map
function onStopSelect(marker) {
	console.log('onStopSelect');

	// if origin selected and the marker is the origin then clear selection, no more path
	if (origin == marker.stop) {
		origin = null;
		destination = null;
		shapes = null;
		info = null;
		refreshMap();
	}
	// if no selection then the marker becomes the origin, partial path
	else if (origin == null) {
		origin = marker.stop;
		destination = null;
		shapes = null;
		info = null;
	}
	// if origin selected then the marker becomes the destination, complete path
	else if ((origin != null) && (destination == null)) {
		destination = marker.stop;
		shapes = null;
		info = null;
	}
	// if origin and destination are already selected then the marker becomes the new origin, partial path
	else if ((origin != null) && (destination != null)) {
		origin = marker.stop;
		destination = null;
		shapes = null;
		info = null;
	}

	// if we have just the origin, find possible routes
	if ((origin != null) && (destination == null)) {
		findRoutes(origin.stop_id, function(data) {
			info = data;
			//shapes = data.shapes;
			// force refresh
			refreshMap();
		});
	}
	// if we have a pair fetch the route and show the detail view
	else if ((origin != null) && (destination != null)) {
		// find next departures
		findItinerary(origin.stop_id, destination.stop_id, function(data) {
			info = data;
			// force refresh
			refreshMap();
		});
	}

}

function findItinerary(oid, did, fn) {
	console.log('findItinerary');
	url = "/script/findItinerary?origin_id=" + oid + "&destination_id=" + did;
	GDownloadUrl(url, function(obj) {
		alert(obj);
		var data = eval('(' + obj + ')');
		if (fn != null)
			fn(data);
	});
}

function findRoutes(id, fn) {
	console.log('findRoutes');
	url = "/script/findRoutes?stop_id=" + id;
	GDownloadUrl(url, function(obj) {
		var data = eval('(' + obj + ')');
		if (fn != null)
			fn(data);
	});
}

