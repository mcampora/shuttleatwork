package shuttleatwork.actions

import shuttleatwork.model.*
import shuttleatwork.system.*

/**
 * returns the list of ordered paths the network contains
 * (start point, way points, end point)
 */
def paths = Facade.getInstance().getGraph(req.feed[0]).getPaths()
def res = JSon2.transform(paths)
//println res
return res
