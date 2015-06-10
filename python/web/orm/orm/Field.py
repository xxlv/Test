class Field(object):
	def __init__(self,name,colum_type):
		self.name=name
		self.colum_type=colum_type

	def __str__(self):
		return '<%s %s>'%(self.__class__.__name__,self.name)

class StringField(Field):
	def __init__(self,name):
		super(StringField,self).__init__(name,'varchar(100)')
calss IntegerField(Field):
	def __init__(self,name):
		super(IntegerField,self).__init__(name,'bigint')
							
