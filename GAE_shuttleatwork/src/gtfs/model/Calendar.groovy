package gtfs.model

/**
 * Week map, helps clarifying when a given service is available (ex. does not work on Monday)
 * Can have different week maps depending on the period of the year (ex. summer schedule)
 */
class Calendar {
	String service_id;
	String monday;
	String tuesday;
	String wednesday;
	String thursday;
	String friday;
	String saturday;
	String sunday;
	String start_date;
	String end_date;

	String toString() {
		return "[ service_id:'$service_id', monday:'$monday', tuesday:'$tuesday', " +
		"wednesday:'$wednesday', thursday:'$thursday', friday:'$friday', saturday:'$saturday', " +
		"sunday:'$sunday', start_date:'$start_date', end_date:'$end_date' ]";
	}
}
