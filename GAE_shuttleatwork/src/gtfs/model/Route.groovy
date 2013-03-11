package gtfs.model

/**
 * Graphical attributes of a given route
 */
class Route {
	String route_id;
	String agency_id;
	String route_short_name;
	String route_long_name;
	String route_desc;
	String route_type;
	String route_url;
	String route_color;
	String route_text_color;

	String toString() {
		return "[ route_id:'$route_id', agency_id:'$agency_id', route_short_name:'$route_short_name', " +
		"route_long_name:'$route_long_name', route_desc:'$route_desc', route_type:'$route_type', route_url:'$route_url', " +
		"route_color:'$route_color', route_text_color:'$route_text_color' ]";
	}
}
