package utils.geo

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
    // orthodromic distance
    // Ortho(A,B)=6371 x acos[cos(LatA) x cos(LatB) x cos(LongB-LongA)+sin(LatA) x sin(LatB)]
    double res =
      Math.acos(
        Math.cos(lat) * Math.cos(pos.getLat()) * Math.cos(pos.getLon() - lon) +
        Math.sin(lat) * Math.sin(pos.getLat())
        ) * 6371.0;
    return res;
  }

  String toString() {
    return "[ lat:'$lat', lon:'$lon' ]";
  }
}
