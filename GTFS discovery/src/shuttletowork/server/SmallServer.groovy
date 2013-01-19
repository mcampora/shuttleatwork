package shuttletowork.server

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import shuttletowork.system.*

class BaseHandler implements com.sun.net.httpserver.HttpHandler {
  def service
  def context

  BaseHandler(def ctx, def svc) {
    context = ctx
    service = svc
  }

  def splitURI(def uri) {
    String path = uri.getPath()
    if (path.length() > context.length() + 1) {
      path = path.substring(context.length() + 1)
    }
    def query = [:]
    if (uri.getQuery() != null) {
      uri.getQuery().split("&").each {
        def param = it.split("=");
        query[param[0]] = param[1]
      }
    }
    def res = [path, query]
    return res
  }

  void handle(HttpExchange t) throws IOException {
    def status = 200
    try {
      def req = splitURI(t.getRequestURI())
      def response = service.execute(req[0], req[1])
      if (response == null) {
        response = []
        status = 404
        println 'File not found!'
      }
      t.sendResponseHeaders(status, response.size())
      OutputStream os = t.getResponseBody()
      os.write(response)
      os.close()
    }
    catch (Exception e) {
      println "Unexpected issue " + e
      e.printStackTrace()
    }
  }
}

String FILE_ROOT = "${Env.ROOT}/home/"
String SCRIPT_ROOT = "${Env.ROOT}/src/shuttletowork/actions/"
def PORT = 8000

println ">Init server..."
HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0)
server.createContext("/file", new BaseHandler("/file", new FileService(FILE_ROOT)))
server.createContext("/script", new BaseHandler("/script", new ScriptService(SCRIPT_ROOT)))
server.setExecutor(null)
println "connect to http://localhost:$PORT/file/index.html"
server.start()
