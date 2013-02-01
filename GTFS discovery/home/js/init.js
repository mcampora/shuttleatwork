var map;
var iconOrigin;
var iconDestination;
var iconBackground;
var stops;
var trips;
var routes;
var shapes;
var name;

// Initialize the map, zoom to fit with the selected feed(s), plot the stops
function load() {
	if (GBrowserIsCompatible()) {
		// create the map widget
		var map_dom = $("#map")[0];
		map = new GMap2(map_dom);
		map.addControl(new GSmallZoomControl3D());
		//map.addControl(new GMapTypeControl());
		//map.addControl(new GOverviewMapControl());
		map.enableScrollWheelZoom();
		map.enableDoubleClickZoom();
		loadFeed("1Ashuttle");
	}
}

function loadFeed(name) {
	//TBD: load the feed server-side

	// retrieve the feed area and size the map
	getArea(function(area) {
		var bb = new GLatLngBounds(new GLatLng(area.min.lat, area.min.lon),new GLatLng(area.max.lat, area.max.lon));
		map.setCenter(bb.getCenter(), map.getBoundsZoomLevel(bb));
	});

	// set callbacks
	GEvent.addListener(map, "moveend", refreshMap);
	//GEvent.addListener(map, "zoomend", callbackZoomEnd);

	// create bus stops icons
	initIcons();

	$("#nw")[0].innerText = "GTFS Viewer: " + name;

	// load bus stops and plot them on the map (pretend the map moved)
	// add them also to the list in the sidebar
	getStops(refreshMap);

	// load also routes and trips
	getTrips();
	getRoutes();
	getShapes();
}

//get the geographical area containing the current feed(s)
function getArea(fn) {
	url = "/script/getArea";
	GDownloadUrl(url, function(obj) {
		var area = eval('(' + obj + ')');
		//alert(obj);
		if (fn != null)
			fn(area);
	});
}

// create icons for the stops
function initIcons() {
    iconDestination = makeStopIcon();
    iconDestination.image = "/file/icons/yellow_bus.png";
    iconDestination.shadow = "/file/icons/bus_shadow.png";

    iconOrigin = makeStopIcon();
    iconOrigin.image = "/file/icons/green_bus.png";
    iconOrigin.shadow = "/file/icons/bus_shadow.png";

    iconBackground = makeStopIcon();
    iconBackground.image = "/file/icons/bus.png";
    iconBackground.shadow = "/file/icons/bus_shadow.png";
}
function makeStopIcon() {
    var icon = new GIcon();
    icon.iconSize = new GSize(36, 40);
    //icon.shadowSize = new GSize(46, 40);
    icon.iconAnchor = new GPoint(18, 40);
    icon.infoWindowAnchor = new GPoint(5, 1);
    return icon;
}

function getContent(url, fn) {
	GDownloadUrl(url, function(msg) {
		//console.log(msg);
		var obj = eval('(' + msg + ')');
		if (fn != null)
			fn(obj);
	});
}

//get the stops and their respective position
function getStops(fn) {
	getContent("/script/getStops", function(obj){
		stops = obj;
		if (fn != null) fn(stops);
	});
}

//get the trips
function getTrips(fn) {
	getContent("/script/getTrips", function(obj){
		trips = obj;
		if (fn != null) fn(trips);
	});
}

//get the routes
function getRoutes(fn) {
	getContent("/script/getRoutes", function(obj){
		routes = obj;
		if (fn != null) fn(routes);
	});
}

//get the shapes
function getShapes(fn) {
	getContent("/script/getShapes", function(obj){
		shapes = obj;
		if (fn != null) fn(shapes);
	});
}
