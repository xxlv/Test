#!/usr/bin/python
#coding:utf-8

#author:	gavingeng
#date:		2011-12-03 10:50:01 

class Person:

	def __init__(self):
		print ("init")

	@staticmethod
	def sayHello(hello):
		if not hello:
			hello='hello'
		print ("i will sya %s" %hello)


	@classmethod
	def introduce(clazz,hello):
		clazz.sayHello(hello)
		clazz.hello(clazz,"")
		print("clazz===>",type(clazz))
		print("from introduce method")

	def hello(self,hello):
		self.sayHello(hello)
		print("self===>",type(self))
		print("from hello method")		


def main():
	Person.sayHello("haha")
	Person.introduce("hello world!")
	#Person.hello("self.hello")	#TypeError: unbound method hello() must be called with Person instance as first argument (got str instance instead)
	
	print ("*" * 20)
	p = Person()
	p.sayHello("haha")
	p.introduce("hello world!")
	p.hello("self.hello")

if __name__=='__main__':
	main()
