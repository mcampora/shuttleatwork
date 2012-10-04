package facade

import server.*

def res = Facade.getInstance().feed.getStops().values().asList()
println res
def sres = JSon.transform(res)
println sres
return sres
