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

    // initialize the mapcanvas, load the network,
    // create the markers and register event listeners
    initialize: function() {
    	console.log('initialize');

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
	                new google.maps.LatLng(area.min.lat,
	                        area.min.lon), // sw
	                new google.maps.LatLng(area.max.lat,
	                        area.max.lon))); // ne

            // load bus stops and plot them on the map
            network.getStops(function(stops) {
                mapview.setMarkers(mapview.map, stops);

                // load also routes and trips
                network.getRoutes(function(routes) {
                    network.getTrips(function(trips) {
                    	//network.getShapes(function(shapes) {

                    		// remove the spinner
                        	$.mobile.loading("hide");
                        	
                        	// force landscape mode
							$( window ).on( "orientationchange", function( event ) {
								var orientation = event.orientation;
								console.log("orientationchange: " + orientation);
	                   			if (orientation == "portrait") {
									$("#tiltPopup").popup({ dismissible: false });
									$("#tiltPopup").popup("open");
	                   			}
	                   			else {
									$("#tiltPopup").popup("close");
	                   			}
							});
							$( window ).orientationchange();
							
							// build shapes for the routes dynamically
							// using Google directions
							mapview.shapes = mapview.buildShapes();
                    	//});
                	});
                });
            });
        });
    },

    buildShapes: function() {
    	var shapes = {};
    	// get the different paths in the network
    	network.getPaths(function(paths) {
			var i = 1;
    		paths.forEach(function(obj) { 
				var name = "SH" + i;
				var shape = mapview.buildShape(name, obj);
				shapes[name] = shape;
				i = i + 1;
			});
    	});
    	return shapes;
    },
    
    buildShape: function(name, path) {
		//console.log(path); 
    	//var directionsDisplay = new google.maps.DirectionsRenderer();
    	//directionsDisplay.setMap(map);
    	var directionsService = new google.maps.DirectionsService();
    	var coord = function(stop_id) {
    		//console.log(stop_id);
        	var s = stops[stop_id];
        	var p = new google.maps.LatLng(s.stop_lat, s.stop_lon);
        	return p;
    	}
       	var origin = coord(path[0]);
    	var destination = coord(path.slice(-1));
    	var waypoint_ids = path.slice(1,-1);
    	var waypoints = [];
    	waypoint_ids.forEach(function(obj){
    		waypoints.push({
    	          location:coord(obj),
    	          stopover: false
    	    });
    	});
    	var request = {
    			origin: origin,
    			destination: destination,
    			waypoints: waypoints,
    			travelMode: google.maps.TravelMode.DRIVING,
    			unitSystem: google.maps.UnitSystem.METRIC,
    			optimizeWaypoints: false,
    			provideRouteAlternatives: false
    	};
    	var ret = { shape_id: name, shape_pts: [] };
    	directionsService.route(request, function(result, status) {
    		if (status == google.maps.DirectionsStatus.OK) {
    			//console.log(JSON.stringify(result));
    			//directionsDisplay.setDirections(result);
    			var s = 0;
    			result.routes[0].overview_path.forEach(function(point) {
    				ret.shape_pts.push({
    					shape_pt_id: name,
    					shape_pt_lat: point.jb, // lat
    					shape_pt_lon: point.kb, // long
    					shape_pt_sequence: s
    				});
    				s = s + 1;
    			});
    		}
    	});
    	return ret;
    },
    
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
		    strokeOpacity: 0.3,
		    strokeWeight: 4
		});
		return line;
    },
    
    // add a marker for each stop to the map
    setMarkers: function(map, stops) {
        console.log('setMarkers');
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
	    console.log('onStopSelect');
 
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
	    }
	    // if no selection then the marker becomes the origin,
	    // details on next departures and the various shapes are on
	    else {
	        mapview.origin = marker.stop;
	        mapview.origin.marker.setIcon(mapview.selectedIcon);
	        network.findRoutesAndNextDepartures(mapview.origin.stop_id, function(data) {
	            mapview.info = data;
	        	mapview.displayDetailsPannel(marker, mapview.info);
	        	// remove the spinner
	        	$.mobile.loading("hide");

	        });
	    }
	},

	displayDetailsPannel: function(marker, info) {
    	console.log('displayDetailsPannel');
        var html = "<h3 id='stop'>"+ marker.stop.stop_name + "</h3>";
		html = html + "<div id='shuttle' data-role='collapsible-set' data-theme='az' data-content-theme='az' data-mini='true' data-corners='true'>";
		var lines = [];
		for (var route_id in info) {
			var route = routes[route_id];
			// name of the route
			//html = html + "<div data-role='collapsible' data-collapsed='false' style='background-color:#" + route.route_color + ";color:#" + route.route_text_color + "'>";
			html = html + "<div data-role='collapsible' data-mini='true' data-collapsed='false' data-inset='false' style='background-color:#" + route.route_color + "'>";
			html = html + "<h6>" + route.route_long_name + " (" + route.route_short_name + ")</h6>";
			var arcs = info[route_id];
			for (var i=0,len=arcs.length; i<len; i++) {
		    	var arc = arcs[i];
		    	var destination = stops[arc.destination_id];
				html = html + "<ul id='nextstop' data-role='listview' data-theme='c' data-divider-theme='d' data-inset='false'>";
				// name of the next stop
				for (var j=0,jlen=arcs[i].times.length; (j<jlen && j<3); j++) {
	    			var trip = trips[arcs[i].times[j].trip_id]
	    			if (j==0) {
	    				html = html + "<li data-role='list-divider'>" + trip.trip_headsign + "</li>";
	    			}
		    		html = html + "<li>" + arcs[i].times[j].departure_time;
		    		if (arcs[i].times[j].nextDay == true) {
		    			html = html + " (tomorrow)";		    			
		    		}
		    		html = html + "</li>";

			    	// build a polyline for each shape
		    		mapview.shapes[trip.shape_id].color = routes[trip.route_id].route_color;
		    		var line = mapview.buildLine(mapview.shapes[trip.shape_id]);
				    line.setMap(mapview.map);
				    lines.push(line);
				}
				if (j==0) {
		    		html = html + "<li><i>No departures</i></li>";
				}
				html = html + "</ul>";
			}
			html = html + "</div>";
		}
		html = html + "</div>";
		$("#details-content")[0].innerHTML = html;
		$("#details").trigger("create");
    	$("#details").panel("toggle");
    	$("#details").panel({
    		beforeclose: function(event, ui) {
    			console.log("beforeclose");
    	        mapview.origin.marker.setIcon(mapview.normalIcon);
    	        mapview.origin = null;
    	        lines.forEach(function(line) { line.setMap(null); })
    		}
    	});
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