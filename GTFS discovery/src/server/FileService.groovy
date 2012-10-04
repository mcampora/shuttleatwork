package server

import facade.*

class FileService {
  def root

  def FileService(def path) {
    root = path
  }

  def execute(String name, def req) {
    println ">FileService: " + name
    def resp = null
    try {
      File f = new File(root + name)
      if (f.exists()) {
        resp = f.getBytes()
      }
    }
    catch (IOException ex) {
      println "IOException: " + ex
    }
    return resp
  }

  // test method
  public static void main(String[] args) {
	  def ss = new FileService("${Facade.ROOT}/home/")
	  println ss.execute("index.html", null)
	  println ss.execute("bus.png", null)
  }
}
