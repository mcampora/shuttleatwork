package shuttletowork.server

class JSon {
  static def transform(def obj) {
    return toJson("  ", obj, new IdentityHashMap())
  }

  static def toJson(def prefix, def obj, def map) {
    // if null, stop recursion, return empty string
    if (obj == null) {
      return ""
    }

    // if already streamed, stop recursion, return a reference
    def ref = map[obj]
    if (ref != null) {
      println ref
      return "{ 'jsonref':${ref} }"
    }

    def res = ""
    // if simple string, return its JSon representation
    if ((obj instanceof String) || (obj instanceof GString)) {
      res += "'$obj'"
    }
    // if simple Double, return its JSon representation
    else if (obj instanceof Double) {
      res += obj
    }
    // if it is an Array or a List
    else if (obj instanceof List) {
      // remember the object to avoid infinite loops
      map[obj] = map.size() + 1
      // open bracket
      res += "[ "
      // iterates over the elements
      obj.each { o ->
        // process the element
        def val = toJson(prefix + ' ', o, map)
        if (!val.equals(""))
          res += val + ", "
      }
      // close bracket
      res += " ]"
    }
    // if it is a Map
    else if (obj instanceof Map) {
      // remember the object to avoid infinite loops
      map[obj] = map.size() + 1
      // open brace, add the object reference
      res += "{ 'jsonid':${map.size()}, "
      // iterates over the properties
      obj.each { e ->
        // process the property
        def val = toJson(prefix + ' ', e.value, map)
        if (!val.equals(""))
          res += "'${e.key}':"
		  res += val + ", "
      }
      // close brace
      res += " }"
    }
    // in all the other objects
    else if (obj.class.toString().matches(".*(AA|BB|gtfs|geo|graph).*")) {
      // remember the object to avoid infinite loops
      map[obj] = map.size() + 1
      // open brace, add the object reference
      res += "{ 'jsonid':${map.size()}, "
      // iterates over the properties
      obj.properties.each { e ->
        // process the property
        def val = toJson(prefix + ' ', e.value, map)
        if (!val.equals(""))
          res += "'${e.key}':" + val + ", "
      }
      // close brace
      res += " }"
    }
    else {
      println "class: ${obj.class}"
    }
    return res
  }

  static class AA {
    def s;
    def bb;
    }
  static class BB {
    def s;
    def aa;
    }

  public static void main(String[] args) {
    try {
      println transform("test")
	  println "-----------------"

      def b = []
      def a = [ "1", b ]
      b.add(a)
      println transform(a)
	  println "-----------------"

      def c = [:]
      def d = [:]
      c["d"] = d
      d["c"] = c
      println transform(c)
	  println "-----------------"

      def aa = new AA()
      def bb = new BB()
      aa.s = "aa"
      aa.bb = bb
      bb.s = "bb"
      println transform(aa)
      bb.aa = aa
      println transform(aa)
	  println "-----------------"
    }
    catch (Error e) {
      e.printStackTrace()
    }
  }
}