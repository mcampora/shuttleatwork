package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*

/**
 * given a stop returns all possible routes
 */

//println req.stop_id
def routes = Facade.getInstance().getGraph(req.feed[0]).findRoutes(req.stop_id[0])
//println routes
def res = JSon2.transform(routes)
//println res
return res
