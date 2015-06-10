import pymysql as DB

class Db(object):

	@staticmethod
	def create_engine(**conf):
		db_host=conf.get('host','localhost')
		db_port=conf.get('port',3306)
		db_name=conf.get('db_name',None)
		db_user_name=conf.get('user_name','root')
		db_user_pass=conf.get('user_pass','')
		try:
			db_handle=DB.connect(host=db_host,user=db_user_name,password=db_user_pass,port=db_port,database=db_name)
		except Exception:
			print("Conn Error")
			db_handle=None
		return db_handle

			
#测试
#==========================================================================================
if __name__=="__main__":
	db_hanble=Db.create_engine(db_name="youshi")
	db_hanble_cur=db_hanble.cursor()
	db_hanble_cur.execute('select * from ys_job')
	print(db_hanble_cur.fetchall())
#==========================================================================================
