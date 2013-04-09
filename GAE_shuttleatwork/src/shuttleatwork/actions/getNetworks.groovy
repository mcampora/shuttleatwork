package shuttleatwork.actions

def res = Facade.getInstance().feed.getPaths()
def sres = JSon2.transform(res)
//println sres
return sres
