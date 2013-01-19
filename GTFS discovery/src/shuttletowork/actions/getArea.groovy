package shuttletowork.actions

import shuttletowork.system.*
import shuttletowork.model.*

/**
 * Return the area enclosing all the stops the feed contains
 */

def res = JSon.transform(Facade.getInstance().feed.getArea())
//println res
return res