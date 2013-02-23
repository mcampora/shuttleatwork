package shuttletowork.actions

import shuttletowork.system.*
import shuttletowork.model.*
import gtfs.graph.*

/**
 * given a stop and a time returns all possible routes and next departure times
 */

def routes = Facade.getInstance().graph.findRoutesAndTimes(req.stop_id, 
		Time.fmt.format(Calendar.getInstance().getTime()))
def res = JSon2.transform(routes)
return res
