package utils.csv

class Reader {
	def clazz
	def columnmap

	def Reader(def clazz, def columnmap) {
		this.clazz = clazz
		this.columnmap = columnmap
	}

	/*
	 * accept a file name as input, a class type, a map of closure to call when
	 * a column contains a key to be replaced with the associated value
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
	 * @return T instance
	 */
	def translateLine(def head, def line) {
		def obj = clazz.newInstance()
		def data = line.split(",")
		data.eachWithIndex { val, i ->
			def col = head[i]
			def column = columnmap[col]
			if (column) {
				val = column(val)
			}
			obj."$col" = val
		}
		return obj
	}

}