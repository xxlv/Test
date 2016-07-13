# import logging
# import pdb
#
#
#
#
#
# def foo(s):
#     n=int(s)
#     logging.info('n = %d' % n)
#     pdb.set_trace()
#     assert n!=0 ,"n is zero"
#     return 10/n
#
# def main():
#     foo('0')
#
#
#
# # logging.basicConfig(level=logging.INFO)
#
# main()


class Dict(dict):
    def __init__(self,**kw):
        supper().__init__(**kw)

    def __getattr__(self,key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"Dict object has no attribute '%s'"% key)
    def __setattr__(self,key,value):
        self[key]=value
                
