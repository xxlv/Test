#-*- coding:utf-8 -*-

from datetime import datetime, timezone, timedelta


def to_timestamp(dt_str,tz_str):
    
    h=int(tz_str.split("UTC")[1][:-3])
    
    tz=timezone(timedelta(hours=h))

    t=datetime.strptime(dt_str,"%Y-%m-%d %H:%M:%S").replace(tzinfo=tz)
   
    tt=t.timestamp()
    
    
    return tt




t1 = to_timestamp('2015-6-1 08:10:30', 'UTC+7:00')
t2 = to_timestamp('2015-5-31 16:10:30', 'UTC-09:00')


assert t1 == 1433121030.0, t1
assert t2==1433121030.0,t2
