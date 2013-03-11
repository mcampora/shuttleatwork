package shuttleatwork.actions

def res = Facade.getInstance().feed.getNetworks()
def sres = JSon2.transform(res)
//println sres
return sres
