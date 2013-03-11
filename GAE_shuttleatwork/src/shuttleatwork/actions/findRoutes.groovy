package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*

/**
 * given a stop returns all possible routes
 */

//println req.stop_id
def routes = Facade.getInstance().graph.findRoutes(req.stop_id[0])
//println routes
def res = JSon2.transform(routes)
//println res
return res
