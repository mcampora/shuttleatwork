package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*

/**
 * returns the list of stops the feed contains with all the details that
 * will be used for display
 */

def res = Facade.getInstance().feed.getStops()
def sres = JSon2.transform(res)
//println sres
return sres