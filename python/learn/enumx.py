from enum import Enum

Month=Enum('Month',('1','2','3'))
for name,member in Month.__members__.items():
    pass
    # print(name,'=>',member,',',member.value)
#
#
# class Hello(object):
#     def hello(self,name='world'):
#
#         print('hello %s' % name)
#
#
# def fn(self):
#     pass
#
#
# W=type('World',(object,),dict(hello=fn))
# w=W()
# print(type(w))
# print(type(W))
l={"1":"1"}

print(l.items)
