package shuttletowork.actions

import shuttletowork.system.*
import shuttletowork.model.*

/**
 * given a stop returns all possible routes
 */

//println req.stop_id
def routes = Facade.getInstance().graph.findRoutes(req.stop_id)
//println routes
def res = JSon.transform(routes)
//println res
return res
