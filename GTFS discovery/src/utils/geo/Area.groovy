package utils.geo

/**
 * Rectangular area based on mininum 2 geolocations
 * Can be used to compute the enclosing area for a set of locations
 */
class Area {
  Position max = new Position(-180.0, -180.0);
  Position min = new Position(180.0, 180.0);

  def Area() {
  }

  def Area(def min, def max) {
	  this.min = min
	  this.max = max
  }

  def Area(def minlat, def minlon, def maxlat, def maxlon) {
	  this.min = new Position(minlat, minlon)
	  this.max = new Position(maxlat, maxlon)
  }

  def extend(Position pos) {
    if (pos.getLat() < min.getLat())
      min.setLat(pos.getLat());
    if (pos.getLon() < min.getLon())
      min.setLon(pos.getLon());
    if (pos.getLat() > max.getLat())
      max.setLat(pos.getLat());
    if (pos.getLon() > max.getLon())
      max.setLon(pos.getLon());
  }

  def extend(Area area) {
    this.extend(area.getMin());
    this.extend(area.getMax());
  }

  String toString() {
    return "[ min:'$min', max:'$max' ]";
  }
}
