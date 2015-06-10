##偏函数 现有的函数可能通过参数值不同有不同的意义，偏函数将为这些函数快速创建别名
##new_fun=functools.partical(fun_name,pra=xx)<=>new_fun=fun_name(pra=xx)
##
import functools

sorted_ignore_case = functools.partial(sorted,cmp=lambda s1,s2:cmp(s1.upper(),s2.upper()))

print sorted_ignore_case(['bob', 'about', 'Zoo', 'Credit'])