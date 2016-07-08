from functools import reduce

# def f(x,y):
#     return x*y
# r=map(f,[1,2,3,4,5,7])
#
# print(reduce(f,[1,2,3,4,5]))

def normalize(name):
    return name.capitalize()

#
# CHAR_TO_FLOAT={
#     '0': 0,
#     '1': 1,
#     '2': 2,
#     '3': 3,
#     '4': 4,
#     '5': 5,
#     '6': 6,
#     '7': 7,
#     '8': 8,
#     '9': 9,
#     '.': -1
# }
#
# def str2float(s):
#     l=map(lambda x:CHAR_TO_FLOAT[x],s)
#     p=0
#
#     def to_float(x,y):
#         nonlocal p
#         if(y==-1):
#             p=1
#             return x
#         if(p==0):
#             return 10*x+y
#         else:
#             p=p*10
#             return x+y/p
#
#     r=reduce(to_float,l,0.0)
#     return r

# print(list(map(normalize,['adam', 'LISA', 'barT'])))
# def _odd_iter():
#     n=1
#     while True:
#         n=n+2
#         yield n
#
# def _not_divisible(n):
#     return lambda x:x%2>0
#
# def primes():
#     yield 2
#     it=_odd_iter()
#     while True:
#         n=next(it)
#         yield n
#         it =filter(_not_divisible(n),it)
#
#
#
#
# for n in primes():
#     if n < 1000:
#         print(n)
#     else:
#         break
# def is_palindrome(number):
#     oldnumber=number
#     l=list()
#     number=str(number)
#     length=len(number)
#
#     for n in number:
#         length=length-1
#         l.append(number[length])
#
#     reverse_number=''
#
#     for n in l:
#         reverse_number+=n
#
#     print("%s => %s "%(oldnumber,reverse_number))
#     if(int(reverse_number)==int(oldnumber)):
#         return True
#     else:
#         return False
#
# output = filter(is_palindrome, range(1, 1000))
#
# print(list(output))

def test_func():
    pass
    
print(test_func.__name__)
