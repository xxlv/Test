module.exports=function(name,age){
	var self=this;

	self.name=name;
	self.age=age;
	self.say=function(){
		console.log('Hello !');
	}

}