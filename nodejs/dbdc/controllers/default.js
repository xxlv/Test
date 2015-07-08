exports.install = function(FS) {
    FS.route('/*', view_app);
    FS.route('/hello', view_hello);
    FS.route('/json', json_contact);


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