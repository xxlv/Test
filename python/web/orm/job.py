from flask import Flask
import lorm
import db 
import sys


app = Flask(__name__)

class Job(lorm.Model):

	__table__="ys_job"
	
	title=lorm.StringFiled("title")
	company=lorm.StringFiled("company")
	job=lorm.StringFiled("job")
	hire_date=lorm.StringFiled("hire_date")
	status=lorm.StringFiled("status")
	money=lorm.StringFiled("money")
	accept_number=lorm.StringFiled("accept_number")
	detail=lorm.StringFiled("detail")
	pay_way=lorm.StringFiled("pay_way")
	start_date=lorm.StringFiled("start_date")
	end_date=lorm.StringFiled("end_date")
	linkman=lorm.StringFiled("linkman")

d=Job()
d.fetchall()
# d=Job(title="HEHE",company="1",hire_date=1,status=1,accept_number=1)
# d.save()

@app.route('/')
def index():
    return "Hello, World!"

if __name__ == '__main__':
    app.run(debug=True)