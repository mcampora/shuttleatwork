var origin  = null; // keep track of the user selection
var info    = null; // if not null then the info window is displayed with its content

// fired when the map viewpoint or content is changed
function refreshMap() {
	console.log('refreshMap');
	map.clearOverlays();
	for (var stop_id in stops) {
		if (stop_id != "jsonid") {
			var stop = stops[stop_id];
			addStopMarker(stop);
		}
	}
	if (info != null)
		displayDetails(origin.marker, info);
}

// add a marker on the map corresponding to a given stop
function addStopMarker(stop) {
    var icon;
    if (stop == origin) {
      icon = iconOrigin;
    }
    else {
      icon = iconBackground;
    }
    var ll = new GLatLng(stop.pos.lat, stop.pos.lon);
    var marker = new GMarker(ll, {icon: icon, draggable: false});
    marker.stop = stop;
    stop.marker = marker;
    map.addOverlay(marker);
    marker.clickListener = GEvent.addListener(marker, "click", function() {onStopSelect(marker);});
}

// called when a stop is selected
function displayDetails(marker, info) {
	console.log('displayDetails');
    var hshapes = [];
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
	    			var shape = shapes[trip.shape_id];
	    			shape.color = route.route_color;
	    			hshapes[trip.shape_id] = shape;
	    		}
	    		html = html + "</ul>";
    		}
    		html = html + "</ul>";
    	}
    }
    html = html + "</div></div>";
    displayShapes(hshapes);
    origin.marker.openInfoWindowHtml(html);
}

// complete displayDetails
function displayShapes(hshapes) {
	console.log('displayShapes');
	for (var nshape in hshapes) {
		var shape = hshapes[nshape];
		//console.log(shape);
		var linePoints = Array();
		for (var i = 0; i < shape.shape_pts.length; i++) {
			var ll = new GLatLng(shape.shape_pts[i].pos.lat, shape.shape_pts[i].pos.lon);
			linePoints[linePoints.length] = ll;
		}
		var color = shape.color;
		var polyline = new GPolyline(linePoints, color, 4);
		map.addOverlay(polyline);
  	}
}

// fired when the user select a stop on the map
function onStopSelect(marker) {
	console.log('onStopSelect');
	// if origin selected and the marker is the origin then clear selection, no more details or shapes
	if (origin == marker.stop) {
		origin = null;
		info = null;
		refreshMap();
	}
	// if no selection then the marker becomes the origin, details on next departures and the various shapes are on
	else {
		origin = marker.stop;
		findRoutes(origin.stop_id, function(data) {
			info = data;
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

