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
		initSettingsPanel();
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

	// load bus stops and plot them on the map (pretend the map moved)
	// add them also to the list in the sidebar
	getStops(refreshMap);

	// load also routes and trips
	getTrips();
	getRoutes();
	getShapes();
}

// create icons for the stops
function initIcons() {
    iconDestination = makeStopIcon();
    iconDestination.image = "/icons/yellow_bus.png";
    iconDestination.shadow = "/icons/bus_shadow.png";

    iconOrigin = makeStopIcon();
    iconOrigin.image = "/icons/green_bus.png";
    iconOrigin.shadow = "/icons/bus_shadow.png";

    iconBackground = makeStopIcon();
    iconBackground.image = "/icons/bus.png";
    iconBackground.shadow = "/icons/bus_shadow.png";
}
function makeStopIcon() {
    var icon = new GIcon();
    icon.iconSize = new GSize(36, 40);
    //icon.shadowSize = new GSize(46, 40);
    icon.iconAnchor = new GPoint(18, 40);
    icon.infoWindowAnchor = new GPoint(5, 1);
    return icon;
}

var name 	= "1Ashuttle";
var closest = false;
var checknext 	= true;
	
// initialiwe the settings page
function initSettingsPanel() {
	$("select#activenw").empty();
	$("select#activenw").append("<option value='" + name + "'>" + name + "</option>");
	
	//if (closest)
	//	$( "select#closest>option[value='true']" ).attr("selected", "true");
	//$( "select#closest" ).slider({
  	//	stop: function( event, ui ) {
  	//		closest = !closest;
  	//	}
	//});

	if (checknext)
		$( "select#checknext>option[value='true']" ).attr("selected", "true");
	$( "select#checknext" ).slider({
  		stop: function( event, ui ) {
  			checknext = !checknext;
  		}
	});
}