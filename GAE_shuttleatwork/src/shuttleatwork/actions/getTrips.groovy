package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*

// deprecated

/**
 * returns the list of stops the feed contains with all the details that
 * will be used for display
 */

def res = Facade.getInstance().getFeed(req.feed[0]).getTrips()
def sres = JSon2.transform(res)
//println sres
return sres
