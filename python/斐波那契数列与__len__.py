class Fib(object):

    def __init__(self, num):
        self.num=num
        l=[]
        a=0
        b=1
        #find shulie
        for x in range(num):
            l.append(a)
            a,b=b,a+b
            #t=a
            #a=b
            #b=t+b
            #以上的语句可以简化成a, b = b, a + b  赋值运算，先计算赋值号（也就是=号左边的，再赋值）
            
        self.l=l
        
    def __str__(self):
        return str(self.l)
        
    def __len__(self):
        return self.num

f = Fib(10)
print(f)
print (len(f))
print("hello word\r\n")