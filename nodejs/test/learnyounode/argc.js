var argvs=process.argv.slice(2);
var sum=0;
for(var i =0;i<argvs.length;i++){
	sum+=Number(argvs[i]);		
}

console.log(sum);
