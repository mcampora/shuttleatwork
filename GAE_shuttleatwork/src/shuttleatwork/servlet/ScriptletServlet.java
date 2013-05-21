package shuttleatwork.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shuttleatwork.server.ScriptService;

@SuppressWarnings("serial")
public class ScriptletServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

        resp.setContentType("text/plain");
		String script = req.getParameter("action");
        if (script != null) {
            try {
            	ScriptService ss = new ScriptService("");
            	String res = ss.execute(script, req.getParameterMap());
            	resp.getWriter().println(res);
            }
            catch (Exception e) {
                resp.sendError(500, "Script failed! " + script + ", " + e.toString());
            }
    }
        else {
            resp.sendError(500, "no action!");
        }
	}

}
