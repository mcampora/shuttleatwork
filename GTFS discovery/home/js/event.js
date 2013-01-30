// keep track of the user selections
var origin = null; // first selection
var destination = null; // second selection

// if not null then the info window is displayed with its content
var info = null; // should be non null after the first selection

// list of routes
var shapes = null; // should be non null after the first selection

// fired when the map viewpoint is changed or there's
// a need to refresh markers, routes and details
function refreshMap() {
	console.log('refreshMap');
	if (stops != null) {
		map.clearOverlays();
	for (var i=0; i<stops.length; ++i) {
		var stop = stops[i];
  		var marker = addStopMarker(stop);
  		stop.marker = marker;
  	}
	if (shapes != null)
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
	html = html + "<span class='stopname'>" + marker.stop.stop_name + "</span><br/>";
    for (var route in info) {
    	if (route != "jsonid") {
    		html = html + "<span class='routename'>" + route + " (" + route + ")</span><br/>";
    		var arcs = info[route];
    		for (var i=0,len=arcs.length; i<len; i++) {
    			var arc = arcs[i]
    			html = html + "<li class='departure'>" + arc.destination_id + "</li><br/>";
	    		for (var j=0,jlen=arcs[i].times.length; (j<jlen && j<3); j++) {
    				html = html + "<li class='departure'>" + 
    					arcs[i].times[0].trip_id + " - " + arcs[i].times[i].departure_time + "</li><br/>";
	    		}
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

