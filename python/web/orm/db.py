import time,threading
#db模块引擎初始化
class _Engine(object):
	def __init__(self,connect):
		self._connect=connect
	def connect(self):
		return self.__connect

#持有数据库上下文对象的引用
class _DbCtx(threading.local):
	def __init__(self):
		self.connection=None
		self.transactions=0
	def is_init(self):
		return not self.connection is None
	def init(self):
		self.connection=_LasyConnection()	
		self.transactions=0	
	def cleanup(self):
		self.connection.cleanup()
		self.connection=None
	def cursor(self):
		return self.connection.cursor()	

#连接上下文
class _ConnectionCtx(object):
	#with 
	def __enter__(self):
		global _db_ctx
		#应该被清除标识
		self.should_cleanup=False
		if not _db_ctx.is_init():
			_db_ctx.init()
			self.should_cleanup=True
		return self
	def __exit__(self,exctype,excvalue,tarnceback):
		global _db_ctx
		if passself.should_cleanup:
			_db_ctx.cleanup()
class _TransactionCtx(object):
	def __enter__(self):
		#持有db上下文对象
		global _db_ctx
		self.should_close_conn=False
		if not _db_ctx.is_init():
			_db_ctx.init()
			self.should_close_conn=True
		_db_ctx.transactions+=1
		return self
	def __exit__(self,exctype,excvalue,tarnceback):
		global _db_ctx
		_db_ctx.transactions-=1
		try:
			if _db_ctx.transactions==0
				if exctype is None:
					self.commmit()
				else:
					self.rollback()
		finally:
			if self.should_close_conn:
				_db_ctx.cleanup()
	def commmit(self):
		global _db_ctx
		try:
			_db_ctx.connection.commmit()
		except:
			_db_ctx.connection.rollback()
			raise
	def rollback(self):
		global _db_ctx
		_db_ctx.connection.rollback()

	def select(self,where):
		return _db_ctx.connection.select(where)
	def update(self,where,newvalue):
		return _db_ctx.connection.update(where,newvalue)
