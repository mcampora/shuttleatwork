package shuttleatwork.actions

import shuttleatwork.system.*
import shuttleatwork.model.*

/**
 * Return the area enclosing all the stops the feed contains
 */

def res = JSon2.transform(Facade.getInstance().feed.getArea())
//println res
return res
