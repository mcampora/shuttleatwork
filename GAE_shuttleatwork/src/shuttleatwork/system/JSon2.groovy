package shuttleatwork.system

class JSon2 {
  static def transform(def obj) {
    def res = ""

    // if null, stop recursion, return empty string
    if (obj == null) {
      return res
    }

    // if simple string, return its JSon representation
    if ((obj instanceof String) || (obj instanceof GString)) {
      res += "\"$obj\""
    }
	
    // if simple Double, return its JSon representation
    else if (obj instanceof Double) {
      res += obj
    }
	
    // if it is an Array or a List
    else if (obj instanceof List) {
      // open bracket
      res += "[ "
      // iterates over the elements
	  def first = true
      obj.each { o ->
        // process the element
        def val = transform(o)
        if (!val.equals("")) {
			// add a separator if it is not the first element
			if (first)
				first = false
			else
				res += ", "
			res += val
        }
      }
      // close bracket
      res += " ]"
    }

    // if it is a Map
    else if (obj instanceof Map) {
      // open brace
      res += "{ "
      // iterates over the properties
	  def first = true
      obj.each { e ->
        // process the property
        def val = transform(e.value)
        if (!val.equals("")) {
			// add a separator if it is not the first element
			if (first)
				first = false
			else
				res += ", "
			res += "\"${e.key}\":"
			res += val
        }
      }
      // close brace
      res += " }"
    }
	
    // in all the other objects
    else if (obj.class.toString().matches(".*(AA|BB|gtfs|geo|graph).*")) {
		// open brace, add the object reference
		res += "{ "
		// iterates over the properties
		def first = true
		obj.properties.each { e ->
			// process the property
			def val = transform(e.value)
			if (!val.equals("")) {
				// add a separator if it is not the first element
				if (first)
					first = false
				else
					res += ", "
				res += "\"${e.key}\":" + val
			}
		}
		// close brace
		res += " }"
    }
    else {
      //println "class: ${obj.class}"
    }
    return res
  }
}
