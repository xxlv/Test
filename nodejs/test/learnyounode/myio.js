//读取一个文件 在stdout输出 行数
var fs=require('fs');
var filename=process.argv[2];
var f_hd=fs.readFileSync(filename);
var context=f_hd.toString();
var len=context.split('\n').length-1;
console.log(len);