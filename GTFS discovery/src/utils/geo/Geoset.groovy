package utils.geo

class Geoset {
  Area area = new Area();

  def positions = [];
  def objects = [];

  void add(Position pos, Object obj) {
    positions.add(pos);
    objects.add(obj);
	area.extend(pos);
  }

  // TBD: stupid, at the moment go through the whole list
  // need a quadtree to divide the plan and reduce complexity
  Object closest(Position pos) {
    Position rpos = null;
    Object robj = null;
    double rdist = Double.MAX_VALUE;
    for (int i = 0; i < positions.size(); i++) {
      double ldist = positions[i].distance(pos);
      if (ldist < rdist) {
        rdist = ldist;
        rpos = positions[i];
        robj = objects[i];
      }
    }
    return robj;
  }
}
