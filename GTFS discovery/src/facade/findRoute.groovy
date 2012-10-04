package facade

import server.*

println req.origin_id + " -> " + req.destination_id
def routes = Facade.getInstance().graph.findRoute(req.origin_id, req.destination_id)
println routes
def res = JSon.transform(routes)
println res
return res
