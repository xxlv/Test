import pymysql.cursors
def hello():
	for i in range(1,20):
		print(i)
#connect to mysql 
connection=pymysql.connect(host='localhost',user='root',passwd='',db="shujuxia2",cursorclass=pymysql.cursors.DictCursor)
try:
	with connection.cursor() as cursor:
		sql="SELECT * FROM `sys_user`"
		cursor.execute(sql)
		result=cursor.fetchone()
		print(result)
finally:
	connection.close()
