package gtfs.model

/**
 * Owner of the feed
 */
class Agency {
	String agency_id;
	String agency_name;
	String agency_url;
	String agency_timezone;

	String toString() {
		return "[ agency_id:'$agency_id', agency_name:'$agency_name', agency_url:'$agency_url', agency_timezone:'$agency_timezone' ]";
	}
}
