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
        map = new google.maps.Map(document
                .getElementById('map-canvas'), mapOptions);

        // retrieve the feed area and resize the map
        network.getArea(function(area) {
	        map.fitBounds(new google.maps.LatLngBounds(
	                new google.maps.LatLng(area.min.lat,
	                        area.min.lon), // sw
	                new google.maps.LatLng(area.max.lat,
	                        area.max.lon))); // ne

            // load bus stops and plot them on the map
            network.getStops(function(stops) {
                mapview.setMarkers(map, stops);

                // load also routes and trips
                network.getRoutes(function(routes) {
                    network.getTrips(function(trips) {
                    	network.getShapes(function(shapes) {

                    		// remove the spinner
                        	$.mobile.loading("hide");
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
							
							// test drive
							//mapview.buildShape("SC1", "GREEN_SIDE_5", "TEMPLIERS");
                    	});
                	});
                });
            });
        });
    },

    buildShape: function(origin_id, destination_id, waypoint_ids) {
    	var directionsDisplay = new google.maps.DirectionsRenderer();
    	directionsDisplay.setMap(map);
    	var directionsService = new google.maps.DirectionsService();
    	var coord = function(stop_id) {
    		//console.log(stop_id);
        	var s = stops[stop_id];
        	var p = new google.maps.LatLng(s.stop_lat, s.stop_lon);
        	return p;
    	}
    	var origin = coord(origin_id);
    	var destination = coord(destination_id);
    	var request = {
    			origin: origin,
    			destination: destination,
    			travelMode: google.maps.TravelMode.DRIVING,
    			unitSystem: google.maps.UnitSystem.METRIC,
    			optimizeWaypoints: false,
    			provideRouteAlternatives: false
    	};
    	directionsService.route(request, function(result, status) {
    		if (status == google.maps.DirectionsStatus.OK) {
    			//console.log(JSON.stringify(result));
    			directionsDisplay.setDirections(result);
    			spy = result;
    		}
    	});
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
					//var shape = shapes[trip.shape_id];
					//if (shape != null)
		   	 			//shape.color = route.route_color;
		    		//hshapes[trip.shape_id] = shape;
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
    		}
    	});
	},

};