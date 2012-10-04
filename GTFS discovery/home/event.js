// fired when the map viewpoint is changed
function onMapMove() {
	console.log('onMapMove');
	if (stops != null) {
		map.clearOverlays();
	for (var i=0; i<stops.length; ++i) {
		var stop = stops[i];
  		var marker = addStopMarker(stop);
  		stop.marker = marker;
  	}
	if (routes != null)
		displayShapes();
	if (info != null)
		displayDetails(origin.marker, info);
  }
}
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
	if (routes != null) {
		for (var p in routes.arcs) {
			var pval = routes.arcs[p][0];
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
	console.log('displayShapes');
	var latLng = marker.getLatLng();
	var timeTrips = ""
    var html = "<div class='stopinfo'>";
	var html = html + "<span class='stopname'>" + marker.stop.stop_name + " (" + marker.stop.stop_id + ")</span><br/>";
    html = html + "<span class='stopposition'>" + "[" + latLng.lat() + ", " + latLng.lng() + "]</span>";
    html = html + "<div class='stopdepartures'>";
    html = html + "<br/>";
    html = html + "SA1 - From Naxos<br/>";
    html = html + "09/08/2012 - 11:45<br/>";
    html = html + "<br/>";
    html = html + "SA2 - To Naxos<br/>";
    html = html + "09/08/2012 - 12:15<br/>";
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
		routes = null;
		info = null;
		onMapMove();
	}
	// if no selection then the marker becomes the origin, partial path
	else if (origin == null) {
		origin = marker.stop;
		destination = null;
		routes = null;
		info = null;
	}
	// if origin selected then the marker becomes the destination, complete path
	else if ((origin != null) && (destination == null)) {
		destination = marker.stop;
		routes = null;
		info = null;
	}
	// if origin and destination are already selected then the marker becomes the new origin, partial path
	else if ((origin != null) && (destination != null)) {
		origin = marker.stop;
		destination = null;
		routes = null;
		info = null;
	}

	// if we have just the origin, find possible routes
	if ((origin != null) && (destination == null)) {
		findRoutes(origin.stop_id, function() {
			// force refresh
			onMapMove();
		});
	}
	// if we have a pair fetch the route and show the detail view
	else if ((origin != null) && (destination != null)) {
		// find next departures
		findNextDepartures(origin.stop_id, destination.stop_id, function(data) {
			info = data;
			// force refresh
			onMapMove();
		});
	}

}

function findNextDepartures(oid, did, fn) {
	console.log('findRotue');
	url = "/script/findRoute?origin_id=" + oid + "&destination_id=" + did;
	GDownloadUrl(url, function(obj) {
		routes = eval('(' + obj + ')');
		//alert(obj);
		if (fn != null)
			fn(routes);
	});
}

function findRoutes(id, fn) {
	console.log('findRoutes');
	url = "/script/findRoutes?stop_id=" + id;
	GDownloadUrl(url, function(obj) {
		routes = eval('(' + obj + ')');
		if (fn != null)
			fn(routes);
	});
}

