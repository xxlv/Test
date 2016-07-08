# @author xxlv
# @github https://github.com/xxlv

obj=
    a:'a'
    f:()->
        'hello'

getWeekBeginAndEnd=()->
    dt    =new Date
    begin =new Date
    end   =new Date
    day=dt.getDay()
    date=dt.getDate()
    begin.setDate(date-day+1)
    end.setDate(date+(5-day+1))
    [begin,end]


var_dump=(obj)->
     (o for o in obj).join('')


getWeekBeginAndEnd()
