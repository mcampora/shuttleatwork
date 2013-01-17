package utils.csv

/**
 * versatile CSV reader
 * by default use the first raw to determine attribute names
 * create and feed objects for each line
 * can use a closure to do extra processing on the object (ex. reference lookup)
 */
class Reader {
	def clazz = Object.class
	def closure = null
	def expando = false

	def Reader() {
		expando = true
	}

	def Reader(def clazz, def closure) {
		this.clazz = clazz
		this.closure = closure
	}

	/**
	 * accept a file name as input, a class type, a map of closure to call when it matches a 
	 * column (ex. replace value with an object lookup)
	 * 
	 * return an array of objects
	 */
	def translate(def file) {
		def res = []
		def head = null
		new File(file).eachLine { line ->
			if (!head) {
				head = line.split(",")
			}
			else {
				def obj = translateLine(head, line)
				res.add(obj)
			}
		}
		return res
	}

	/**
	 * accept a line as input, create an object instance and fill its
	 * properties based on the file header
	 * call provided closure to finalize the creation
	 * @return T instance
	 */
	def translateLine(def head, def line) {
		def obj = clazz.newInstance()
		def data = line.split(",")
		data.eachWithIndex { colvalue, i ->
			def colname = head[i]
			if (expando) {
				obj.metaClass."$colname" = colvalue
			} 
			else {
				obj."$colname" = colvalue
			}
		}
		if (closure) {
			closure(obj)
		}
		return obj
	}
}