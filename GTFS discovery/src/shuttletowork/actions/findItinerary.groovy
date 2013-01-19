package shuttletowork.actions

import shuttletowork.system.*
import shuttletowork.model.*

/**
 * Given an origin and a destination return an itinerary (combining possibly multiples routes)
 */

println req.origin_id + " -> " + req.destination_id
def routes //= Facade.getInstance().graph.findRoute(req.origin_id, req.destination_id)
//println routes
def res = JSon.transform(routes)
//println res
return res
