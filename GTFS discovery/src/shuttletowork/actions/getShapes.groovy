package shuttletowork.actions

import shuttletowork.system.*
import shuttletowork.model.*

/**
 * returns the list of stops the feed contains with all the details that
 * will be used for display
 */

def res = Facade.getInstance().feed.getShapes()
def sres = JSon.transform(res)
//println sres
return sres
