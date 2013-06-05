var mapview = {

    // constants and variable
    normalIcon: {
        url : "/icons/bus.png",
        size : new google.maps.Size(36, 40),
        anchor : new google.maps.Point(18, 40)
    },
    selectedIcon: {
        url : "/icons/green_bus.png",
        size : new google.maps.Size(36, 40),
        anchor : new google.maps.Point(18, 40)
    },
    origin: null, // keep track of the user selection
    info: null,
    map: null,
    shapes: null,
    lines: {},

    // initialize the mapcanvas, load the network,
    // create the markers and register event listeners
    initialize: function() {
    	//console.log('initialize...');

		// activate the spiner
        $.mobile.loading('show', {
            text : "loading the network...",
            textVisible : true,
            theme : "a",
 			textonly : false
        });

        // initiate the map
        var mapOptions = {
            center : new google.maps.LatLng(-34.397, 150.644),
            zoom : 8,
            mapTypeId : google.maps.MapTypeId.ROADMAP
        };
        mapview.map = new google.maps.Map(document
                .getElementById('map-canvas'), mapOptions);

        // retrieve the feed area and resize the map
        network.getArea(function(area) {
	        mapview.map.fitBounds(new google.maps.LatLngBounds(
	                new google.maps.LatLng(area.min.lat, area.min.lon), // sw
	                new google.maps.LatLng(area.max.lat, area.max.lon))); // ne

            // load bus stops and plot them on the map
            network.getStops(function(stops) {
                mapview.setMarkers(mapview.map, stops);

                // load also routes and trips
                network.getRoutes(function(routes) {
                    network.getTrips(function(trips) {
                    	network.getShapes(function(shapes) {
                    		mapview.shapes = shapes;

                    		// remove the spinner
                        	$.mobile.loading("hide");

                        	// force landscape mode
							$( window ).on( "orientationchange", function( event ) {
								var orientation = event.orientation;
								console.log("orientationchange: " + orientation);
	                   			if (orientation == "portrait") {
	                   				console.log('tiltPopup.open');
									$("#tiltPopup").popup({ dismissible: false });
									$("#tiltPopup").popup("open");
									tiltPopup = true;
	                   			}
	                   			else {
									if (tiltPopup == true) {
		                   				console.log('tiltPopup.close');
										$("#tiltPopup").popup("close");
										tiltPopup = false;
									}
	                   			}
							});
							$( window ).orientationchange();

							// build shapes for the routes dynamically
							// using Google directions
							//mapview.shapes = editor.buildShapes();
                    	});
                	});
                });
            });
        });
    },

    // translate a shape (a list of geo coordinates)
    // into a polyline object that can be drawn on a map
    buildLine: function(shape) {
		var linePoints = [];
		shape.shape_pts.forEach(function(pt) {
			linePoints.push(new google.maps.LatLng(pt.shape_pt_lat, pt.shape_pt_lon));
		});
		var line = new google.maps.Polyline({
		    path: linePoints,
			icons: [{
				icon: { path: google.maps.SymbolPath.FORWARD_OPEN_ARROW },
				offset: '100%'
			}],
		    strokeColor: shape.color,
		    strokeOpacity: 0.6,
		    strokeWeight: 4
		});
		return line;
    },

    // translate a shape (a list of geo coordinates)
    // into a polyline object that can be drawn on a map
    // skip first points until it matches a given
    // starting point
    buildLine: function(shape, start) {
    	var capture = false;
		var startg = new google.maps.LatLng(start.lat, start.lon);
		var linePoints = [];
		shape.shape_pts.forEach(function(pt) {
			var ptg = new google.maps.LatLng(pt.shape_pt_lat, pt.shape_pt_lon);
			if (capture == false) {
				//console.log(start.lat + " - " + pt.shape_pt_lat);
				//console.log(start.lon + " - " + pt.shape_pt_lon);
				var dist = google.maps.geometry.spherical.computeDistanceBetween(startg, ptg);
				//console.log(dist);
				if (dist < 20.0) {
					capture = true;
					linePoints.push(ptg);
				}
			}
			else {
				linePoints.push(ptg);
			}
		});
		var line = new google.maps.Polyline({
		    path: linePoints,
			icons: [{
				icon: { path: google.maps.SymbolPath.FORWARD_OPEN_ARROW },
				offset: '100%'
			}],
		    strokeColor: shape.color,
		    strokeOpacity: 0.6,
		    strokeWeight: 4
		});
		return line;
    },

    // add a marker for each stop to the map
    setMarkers: function(map, stops) {
        //console.log('setMarkers');
        for (var stop_id in stops) {
            if (stop_id != "jsonid") {
                var stop = stops[stop_id];
                var myLatLng = new google.maps.LatLng(
                        stop.pos.lat, stop.pos.lon);
                var marker = new google.maps.Marker({
                    position : myLatLng,
                    map : map,
                    icon : mapview.normalIcon,
                    title : stop.stop_name
                });
                marker.stop = stop;
                stop.marker = marker;
                mapview.addMarkerEventListener(marker);
            }
        }
    },

    // attach a click listener to the provided marker, call onStopSelect
    addMarkerEventListener: function(marker) {
        google.maps.event.addListener(marker, 'click',
	        function() { mapview.onStopSelect(marker); });
    },

    // called when a stop is selected
	onStopSelect: function(marker) {
	    //console.log('onStopSelect');

	    // activate the spiner
        $.mobile.loading('show', {
            theme : "a",
 			textonly : false
        });

	    if (mapview.origin != null) {
	        mapview.origin.marker.setIcon(mapview.normalIcon);
	    }
	    // if origin selected and the marker is the origin then
	    // clear selection, no more details or shapes
	    if (mapview.origin == marker.stop) {
	        mapview.origin = null;
	        mapview.info = null;
	        mapview.clearDetailsPannel();
	    }
	    // if no selection then the marker becomes the origin,
	    // details on next departures and the various shapes are on
	    else {
	        mapview.origin = marker.stop;
	        mapview.origin.marker.setIcon(mapview.selectedIcon);
	        network.findRoutesAndNextDepartures(mapview.origin.stop_id, function(data) {
	            mapview.info = data;
	        	mapview.displayDetailsPannel(marker, mapview.info);
	        });
	    }
    	// remove the spinner
    	$.mobile.loading("hide");
	},

	clearDetailsPannel: function() {
    	//console.log('clearDetailsPannel');
    	$( "div[id|=shape]" ).trigger( "collapse" );
        var html = "<h3 id='stop'>Stop info...</h3>";
		$(" #map-details ")[0].innerHTML = html;
		$(" #map-details ").trigger("create");
	},

	displayDetailsPannel: function(marker, info) {
    	console.log('displayDetailsPannel');
    	console.log(marker);
    	console.log(info);

    	$( "div[id|=shape]" ).trigger( "collapse" );

    	// Name of the selected stop as header
        var html = "<h3 id='stop'>"+ marker.stop.stop_name + "</h3>";

        // initiate a collapsibleset
        html = html + "<div id='shuttle' data-role='collapsible-set' data-theme='az' data-content-theme='az' data-mini='true' data-corners='true'>";

        // with one collapsible per route + trip (ex: Greenside 15 shuttle - Main site direction)
		for (var route_id in info) {
			var route = routes[route_id];
			var arcs = info[route_id];
			for (var i=0,len=arcs.length; i<len; i++) {
		    	var arc = arcs[i];
		    	var destination = stops[arc.destination_id];

				// for the next 3 departure times
				for (var j=0,jlen=arcs[i].times.length; (j<jlen && j<3); j++) {
	    			var trip = trips[arcs[i].times[j].trip_id];
	    			if (j==0) {
	    				// initiate the collapsible for the route and trip
	    				html = html + "<div id='" + trip.shape_id + "' data-role='collapsible' data-mini='true' data-collapsed='false' data-inset='false' style='background-color:#" + route.route_color + "'>";
	    				html = html + "<h6>" + route.route_long_name;
	    				if (route.route_short_name)
	    					html = html + " (" + route.route_short_name + ")";
	    				html = html + "<br/>" + trip.trip_headsign + "</h6>";

	    				// set shape color
	    				console.log(trip.shape_id);
			    		var shape = mapview.shapes[trip.shape_id]
	    				console.log(route);
			    		if (shape != null) {
			    			shape.color = route.route_color;
		    				console.log(shape.color);
			    		}

					    // initiate a listview
	    				html = html + "<ul id='nextstop' data-role='listview' data-theme='c' data-divider-theme='d' data-inset='false'>";
	    			}
	    			// add a line to the listview for each departure time
		    		html = html + "<li>" + arcs[i].times[j].departure_time;
		    		if (arcs[i].times[j].nextDay == true) {
		    			html = html + " (tomorrow)";
		    		}
		    		html = html + "</li>";
				}
				if (j==0) {
		    		html = html + "<li><i>No departures</i></li>";
				}
				// close the listview and collapsible
				html = html + "</ul></div>";
			}
		}
		// close the collapsibleset
		html = html + "</div>";

		$(" #map-details ")[0].innerHTML = html;
		$(" #map-details ").trigger("create");
    	$( "div[id|=shape]" ).on( "expand", function( event, ui ) {
			//console.log('Expand ' + event.target.id);
			var line = mapview.lines[event.target.id + '#' + marker.stop.stop_name];
			if (line == null) {
				line = mapview.buildLine(mapview.shapes[event.target.id], marker.stop.pos);
				mapview.lines[event.target.id + '#' + marker.stop.stop_name] = line;
			}
			line.setMap(mapview.map);
    	});
    	$( "div[id|=shape]" ).on( "collapse", function( event, ui ) {
			//console.log('Collapse ' + event.target.id);
			var line = mapview.lines[event.target.id + '#' + marker.stop.stop_name];
			if (line != null) line.setMap(null);
    	});
    	$( "div[id|=shape]" ).trigger( "expand" );
	},

	// complete displayDetails
	displayShapes: function(hshapes) {
		console.log('displayShapes');
		console.log(hshapes);

		for (var nshape in hshapes) {
			var shape = hshapes[nshape];
			console.log(shape);
			var linePoints = [];
			shape.shape_pts.forEach(function(pt) {
				linePoints.push(new google.maps.LatLng(pt.shape_pt_lat, pt.shape_pt_lon));
			});
			var line = new google.maps.Polyline({
			    path: linePoints,
				icons: [{
					icon: { path: google.maps.SymbolPath.FORWARD_OPEN_ARROW },
					offset: '100%'
				}],
			    strokeColor: shape.color,
			    strokeOpacity: 0.5,
			    strokeWeight: 3
			});
		    line.setMap(mapview.map);
	  	}
	}
};