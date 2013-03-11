package utils.geo

/**
 * Geolocation
 */
class Position {
  double lat;
  double lon;

  def Position(String lat, String lon) {
    this.lat = Double.parseDouble(lat);
    this.lon = Double.parseDouble(lon);
  }

  def Position(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  def distance(Position pos) {
	  double earthRadius = 3958.75;
	  double dLat = Math.toRadians(pos.lat-lat);
	  double dLng = Math.toRadians(pos.lon-lon);
	  double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	  	Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(pos.lat)) *
		Math.sin(dLng/2) * Math.sin(dLng/2);
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	  double dist = earthRadius * c;
	  int meterConversion = 1609;
	  return new Float(dist * meterConversion).floatValue();
  }

  String toString() {
    return "[ lat:'$lat', lon:'$lon' ]";
  }
}
