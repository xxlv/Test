exports.install = function() {
    F.route('/*', view_app);
    F.route('/user', view_hello);

};

function view_app() {
    var self = this;
    self.view('app');
}

function view_hello(){
	var self=this;
	self.view('hello');

}
function json_contact(){
	var self=this;
	var mode=self.body;
	self.json({success:self.body})



}