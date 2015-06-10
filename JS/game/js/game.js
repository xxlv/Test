  var mycanvas  = document.getElementById("myCanvasTag")
  var ctx = mycanvas.getContext('2d');
  function drawTank(ctx){

  	ctx.strokeStyle="#FF0000";
  	ctx.beginPath();
  	ctx.moveTo(10,10);
	ctx.lineTo(150,10);
  	ctx.moveTo(10,10);
	ctx.lineTo(10,150);
  	ctx.closePath();
  	ctx.stroke();
  }
  drawTank(ctx);
