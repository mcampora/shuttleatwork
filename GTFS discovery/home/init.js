var map;
var iconOrigin;
var iconDestination;
var iconBackground;
var stops;
var origin = null;
var destination = null;
var info = null;
var routes = null;

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

		// retrieve the feed area and size the map
		getArea(function(area) {
			var bb = new GLatLngBounds(new GLatLng(area.min.lat, area.min.lon),new GLatLng(area.max.lat, area.max.lon));
			map.setCenter(bb.getCenter(), map.getBoundsZoomLevel(bb));
		});

		// set callbacks
		GEvent.addListener(map, "moveend", onMapMove);
		//GEvent.addListener(map, "zoomend", callbackZoomEnd);

		// create bus stops icons
		initIcons();

		// load bus stops and plot them on the map (pretend the map moved)
		// add them also to the list in the sidebar
		getStops(onMapMove);
	}
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
    iconDestination.image = "/file/yellow_bus.png";
    iconDestination.shadow = "/file/bus_shadow.png";

    iconOrigin = makeStopIcon();
    iconOrigin.image = "/file/green_bus.png";
    iconOrigin.shadow = "/file/bus_shadow.png";

    iconBackground = makeStopIcon();
    iconBackground.image = "/file/bus.png";
    iconBackground.shadow = "/file/bus_shadow.png";
}
function makeStopIcon() {
    var icon = new GIcon();
    icon.iconSize = new GSize(36, 40);
    //icon.shadowSize = new GSize(46, 40);
    icon.iconAnchor = new GPoint(18, 40);
    icon.infoWindowAnchor = new GPoint(5, 1);
    return icon;
}

//get the stops and their respective position
function getStops(fn) {
	url = "/script/getStops";
	GDownloadUrl(url, function(obj) {
		console.log(obj);
		stops = eval('(' + obj + ')');
		if (fn != null)
			fn(stops);
	});
}
