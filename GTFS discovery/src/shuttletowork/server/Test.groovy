package shuttletowork.server

import geo.*
import gtfs.*
import solver.*
import server.*
import facade.*

def ss = new ScriptService("${Facade.ROOT}/src/")

//println ss.execute("helloWorld", null)
//println ss.execute("findStop", [lat:43.62299, lon:7.071406])
//println ss.execute("getStop", [name:"GREEN_SIDE"])
//println ss.execute("getFeed", null)
//println ss.execute("getArea", null)
//println ss.execute("getStops", null)
println ss.execute("getTrips", null)