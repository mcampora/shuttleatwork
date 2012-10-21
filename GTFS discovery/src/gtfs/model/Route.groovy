package gtfs.model

class Route {
	def route_id;
	def agency; // id
	def route_short_name;
	def route_long_name;
	def route_desc;
	def route_type;
	def route_url;
	def route_color;
	def route_text_color;

	String toString() {
		return "[ route_id:'$route_id', agency:'${agency.getAgency_id()}', route_short_name:'$route_short_name', " +
		"route_long_name:'$route_long_name', route_desc:'$route_desc', route_type:'$route_type', route_url:'$route_url', " +
		"route_color:'$route_color', route_text_color:'$route_text_color' ]";
	}
}
