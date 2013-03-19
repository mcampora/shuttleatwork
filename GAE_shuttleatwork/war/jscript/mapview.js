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
    checknext: false,
    info: null,
    infowindow: null,

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
                    	});
                	});
                });
            });
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
	    if (mapview.infowindow != null) {
	        mapview.infowindow.close();
	    }
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
	        var fn = network.findRoutes;
	        if (mapview.checknext)
	            fn = network.findRoutesAndNextDepartures;
	        fn(mapview.origin.stop_id, function(data) {
	            mapview.info = data;
	        	mapview.displayDetails(marker, mapview.info);
	        });
	    }
	},

	// display the info window with next departures details
    displayDetails: function(marker, info) {
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
					" (" + route.route_short_name + ")</span>";
		
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
						if (shape != null)
		   	 				shape.color = route.route_color;
		    			hshapes[trip.shape_id] = shape;
					}
					if (j==0) {
		    			html = html + "<li><i>No more departures today</i></li>";
					}
					html = html + "</ul>";
				}
				html = html + "</ul>";
		    }
		}
		html = html + "</div></div>";
		// displayShapes(hshapes);
		mapview.infowindow = new google.maps.InfoWindow({ content: html });
		mapview.infowindow.open(marker.get('map'), marker);
	}

};