import time, functools
#
#本例实现了带参数的装饰器，会修改原有函数的__name__等问题
#
#

def performance(unit):
    def perf_decorator(f):
        @functools.wraps(f)
        def wrapper(*args, **kw):
            t1 = time.time()
            r = f(*args, **kw)
            t2 = time.time()
            t = (t2 - t1) * 1000 if unit=='ms' else (t2 - t1)
            print 'call %s() in %f %s' % (f.__name__, t, unit)
            return r
             #wrapper.__name__ = f.__name__
    		 #wrapper.__doc__ = f.__doc__
        return wrapper
    return perf_decorator

#为此函数增加装饰器
@performance('ms')
def factorial(n):
    return reduce(lambda x,y: x*y, range(1, n+1))
print factorial.__name__