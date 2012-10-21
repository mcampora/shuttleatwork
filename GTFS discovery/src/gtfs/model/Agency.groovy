package gtfs.model

class Agency {
	def agency_id;
	def agency_name;
	def agency_url;
	def agency_timezone;

	String toString() {
		return "[ agency_id:'$agency_id', agency_name:'$agency_name', agency_url:'$agency_url', agency_timezone:'$agency_timezone' ]";
	}
}
