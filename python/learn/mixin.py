class Dog(object):
    def hello(self):
        print('Dog')


class SayAble(object):
    name='Satable'
    def say(self):
        print("say %s"%(self.name))




class Cat(Dog,SayAble):
    name='lili'



    def hi(self):
        print("hi ,i am a cat ~")


c=Cat()
c.name="F"
print(Cat.name)

c.say()
c.hello()
