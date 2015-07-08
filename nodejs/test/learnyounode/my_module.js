module.exports=function(dirname,extname,callback){
	var fs=require('fs');
	//需要读取dir下的所有文件
	fs.readdir(dirname,function(err,files){
		if(err) return callback(err);
		callback(null,files);
	})
}

