#-*- coding:utf-8 -*-



eps_c=[]

# 检测Y是否已经被访问过
def visited(y):
    return y in eps_c

# 计算所有X沿着EPS到达的边界
def _eps(x):
    return []

def eps_closure(x):

    """
     求X得EPS闭包 深度优先 
     输出是一个集合

    """
    
    eps_c.append(x)
    
    for y in _eps(x):
        if not visited(y):
            eps_closure(y)

    return eps_c        



print(eps_closure("x"))


