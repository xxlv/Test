var fs=require('fs');
var path=require('path');
//使用异步IO
module.exports=function(dirname,extname,callback){

	var list=new Array();
	fs.readdir(dirname,function(err,files){
		if(err) return callback(err);

		for(var i=0;i<files.length;i++){
			if(path.extname(files[i])=="."+extname){
				list.push(files[i]);	
			}
		}
		return callback(null,list);



	})

};