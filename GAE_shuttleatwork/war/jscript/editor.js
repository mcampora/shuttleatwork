var editor = {
	// Edit function
	// build shapes based on Google directions
	// ask the server for the various paths 
	// (sequences of stops)
	buildShapes: function() {
		var shapes = {};
		// get the different paths in the network
		network.getPaths(function(paths) {
			var i = 1;
			paths.forEach(function(obj) {
				var name = "shape-SH" + i;
				var shape = editor.buildShape(name, obj);
				shapes[name] = shape;
				i = i + 1;
			});
		});
		return shapes;
	},
	
	
    // Edit function
    // build one shape based on one path
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
    			//console.log(result);
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

    // Edit function
    // print shapes in a GTFS format
    // to save shapes computed with 
    // Google directions
    printShapes: function(shapes) {
    	console.log("shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence,shape_dist_traveled");
    	for (k in shapes) { 
    		var shape = shapes[k];
    		shape.shape_pts.forEach(function(pt) {
        		console.log(pt.shape_pt_id + ", " + pt.shape_pt_lat + ", " + pt.shape_pt_lon + ", " + pt.shape_pt_sequence + ", ");
    		});
    	}
    },
    
};