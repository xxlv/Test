var fs=require('fs');
var filename=process.argv[2];
fs.readFile(filename,function(e,d){
	if (e) throw e 
	console.log(d.toString().split('\n').length-1)

});
