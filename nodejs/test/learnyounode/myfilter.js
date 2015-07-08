var fs=require('fs');
var dir=process.argv[2];
var ext_name=process.argv[3];
//需要读取dir下的所有文件

fs.readdir(dir,function(err,files){
	//遍历这个文件数组
	var new_files=[];
	for(var i=0;i<files.length;i++){
		if(files[i].split('.',2)[1]==ext_name){
			console.log(files[i]);		
		}

	}
})


