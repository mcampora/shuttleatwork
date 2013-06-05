package shuttleatwork.actions

def res = Facade.getInstance().getFeed(req.feed[0]).getPaths()
def sres = JSon2.transform(res)
//println sres
return sres
