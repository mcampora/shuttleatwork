package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*
import gtfs.graph.*

/**
 * given a stop and a time returns all possible routes and next departure times
 */
println req.stop_id[0]

def routes = Facade.getInstance().graph.findRoutesAndTimes(req.stop_id[0], 
		Time.fmt.format(Calendar.getInstance().getTime()))
def res = JSon2.transform(routes)
return res
