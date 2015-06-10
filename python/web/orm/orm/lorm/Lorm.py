#coding=utf-8
'Light ORM '
__author__="xxlv"
######################
# d=D(id=1)
# d.save() 
# d.delete(id=1)
# d.craete(id=1)
# d.getOne(id=1)
# d.query(dict('id':1))	
######################
from db import Db 

#字段有自己的名称和类型
class AbstField(object):
	def __init__(self,c_name,c_type):
		self.c_name=c_name
		self.c_type=c_type
	def __str__(self):
		return "<%s %s >"%(self.c_name,self.c_type)	

class StringFiled(AbstField):
	def __init__(self,c_name):
		return super(StringFiled,self).__init__(c_name,"string")

class IntFiled(AbstField):
	def __init__(self,c_name):
		return super(IntFiled,self).__init__(c_name,"int")

#=======================================================================================#
#Model层设计 Model必须持有字段映射的dict,然后拼接sql
#但是Model层获取属性载入到dict中有点难度
#=======================================================================================#
class ModelMetaclass(type):
	def __new__(cls,name,bases,attrs):
		#应该排除Model类，因为这个类不存在mappings等
		if(name=="Model"):
			return type.__new__(cls,name,bases,attrs)
		mappings=dict()
		for k,v in attrs.items():
			#获取到Field
			if(isinstance(v,AbstField)):
				mappings[k]=v

		#删除属性 免得出现意想不到的错误		
		for k in mappings.keys():
			attrs.pop(k)	
		attrs["__mappings__"]=mappings
		attrs["__table__"]=attrs.get('__table__',None)

		return type.__new__(cls,name,bases,attrs)


class Model(dict,metaclass=ModelMetaclass):

	def __init__(self,**kw):
		super(Model,self).__init__(**kw)

	def __getattr__(self,k):
		try:
			return self[k]
		except KeyError:
			raise AttributeError(r"'Model' object has no attribute '%s'" % k)

	def __setattr__(self,k,v):
		self[k]=v	

	def save(self):
		fields =[]
		params =[]
		args = []
		for k, v in self.__mappings__.items():
			fields.append(v.c_name)
			params.append('%s')
			args.append(getattr(self, k, ""))

		db_hanble=Db.create_engine(db_name="youshi")
		sql = 'insert into `%s` (%s) values (%s)' % (self.__table__, ','.join(fields), ','.join(params))
		try:
			with db_hanble.cursor() as cursor:
				cursor.execute(sql, args)
				db_hanble.commit()	
		finally:
			db_hanble.close()

#========================================================================================#
#调用者
#========================================================================================#
class Job(Model):
	__table__="ys_job"
	
	title=StringFiled("title")
	company=StringFiled("company")
	job=StringFiled("job")
	hire_date=StringFiled("hire_date")
	status=StringFiled("status")
	money=StringFiled("money")
	accept_number=StringFiled("accept_number")
	detail=StringFiled("detail")
	pay_way=StringFiled("pay_way")
	start_date=StringFiled("start_date")
	end_date=StringFiled("end_date")
	linkman=StringFiled("linkman")




d=Job(title="HEHE",company="1",hire_date=1,status=1,accept_number=1)
d.save()
db_hanble=Db.create_engine(db_name="shujuxia2")
db_hanble_cur=db_hanble.cursor()
db_hanble_cur.execute('select * from sys_user')
# print(db_hanble_cur.fetchall())


			