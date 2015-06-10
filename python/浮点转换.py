class Rational(object):
    def __init__(self, p, q):
        self.p = p
        self.q = q

    def __int__(self):
        return self.p // self.q   # 这是整处罚

    def __float__(self):
        return float(self.p)/self.q #转换为浮点进行

print float(Rational(7, 2))#调用float 其实是调用了__float__
print float(Rational(1, 3))