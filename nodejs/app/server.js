var http=require('http');

function start(){
	http.createServer(function(request,response){
		response.writeHead(200,{"Content-Type":"text/plain"});
		response.write("Hello word");
		response.end();
		}).listen(8888);


}

exports.start=start;	