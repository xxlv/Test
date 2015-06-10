class Hello(object):
	def hello(self,name='word'):
		print('hello ,%s' %name)
# h=Hello()
#h.hello()
# print(type(Hello))
def fn():
	print("My god！")

Word=type('Word',(object,),dict(hello=fn))
word=Word
word.hello()     