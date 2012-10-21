package gtfs.model

class Calendar {
	def service_id;
	def monday;
	def tuesday;
	def wednesday;
	def thursday;
	def friday;
	def saturday;
	def sunday;
	def start_date;
	def end_date;

	String toString() {
		return "[ service_id:'$service_id', monday:'$monday', tuesday:'$tuesday', " +
		"wednesday:'$wednesday', thursday:'$thursday', friday:'$friday', saturday:'$saturday', " +
		"sunday:'$sunday', start_date:'$start_date', end_date:'$end_date' ]";
	}
}
