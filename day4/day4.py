MIN_VALUE = 138307
MAX_VALUE = 654504

def isok(n : int):
    d = n
    r = n % 10
    pr = 10
    adj = False
    alf = True
    while d > 0:
        if pr < r:
            alf = False
            break
        if r == pr:
            adj = True
        d = int(d / 10)
        pr = r
        r = d % 10
    return alf and adj

def isadeguate(n : int):
    d = n
    r = n % 10
    pr = 10
    alf = True
    streak = 1
    adj_two = False
    while d > 0:
        if pr < r:
            alf = False
            break
        if r == pr:
            streak += 1
        if r != pr:
            if streak == 2:
                adj_two = True
            streak = 1
        d = int(d / 10)
        pr = r
        r = d % 10
    if streak == 2:
        adj_two = True
    return alf and adj_two
        
okcount = 0
adeguatecount = 0
for i in range(MIN_VALUE,MAX_VALUE + 1):
    if isok(i):
        okcount += 1
    if isadeguate(i):
        adeguatecount += 1
print("FIRST HALF ANSWER:" + str(okcount))
print("SECOND HALF ANSWER:" + str(adeguatecount))
