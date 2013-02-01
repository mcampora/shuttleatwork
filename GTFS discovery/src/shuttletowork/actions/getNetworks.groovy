package shuttletowork.actions

def res = Facade.getInstance().feed.getNetworks()
def sres = JSon.transform(res)
//println sres
return sres
