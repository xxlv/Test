#!flask/bin/python
from flask import Flask, jsonify,make_response,abort
from flask.ext.httpauth import HTTPBasicAuth
#version 1.0
#author xxlv
#email lvxiang119@gmail.com

app = Flask(__name__)
auth=HTTPBasicAuth()

tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web', 
        'done': False
    }
]

@app.route('/')
@auth.login_required
def home():
	return jsonify({'hello':'word'})

#兼职信息 获取兼职信息的列表
@app.route('/api/v1/job', methods=['GET'])
def get_tasks_list():
	abort(404)


#兼职信息 根据ID获取一条兼职信息
@app.route('/api/v1/job/<int:job_id>', methods=['GET'])
def get_tasks(job_id):
    return jsonify({'get_job': tasks,'s':job_id})


@app.route('/api/v1/job',methods=['POST'])
def create_job():
    return jsonify({'get_job': tasks,'s':job_id})

#删除兼职的信息	
@app.route('/api/v1/job/<int:job_id>',methods=['DELETE'])
def delete_job(job_id):
	return "1"

@auth.get_password
def get_password(useranme):
	if useranme=='ok':
		return 'python'
	return None	

@app.errorhandler(404)
def not_found(error):
	return make_response(jsonify({'error':'Not Found'}),404)	

@auth.error_handler
def unauthorized():
	return make_response(jsonify({'error':'unauthorized'}),403)

if __name__ == '__main__':
    app.run(debug=True)