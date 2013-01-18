package shuttletowork.server

import shuttletowork.system.*

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

}
