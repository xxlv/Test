//遍历这个文件数组
	

var ff=require('./my_module');

ff(process.argv[2],process.argv[3],filter);

function filter(files,extname){
	var new_files=[];
	for(var i=0;i<files.length;i++){
		if(files[i].split('.',2)[1]==extname){
			console.log(files[i]);		
		}

	}
}
