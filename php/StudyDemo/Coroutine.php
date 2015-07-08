<?php
/*PHP协程体验*/
function my_range($start,$end,$step=1){
	$i=0;
	for($i=$start;$i<=$end;$i+=$step){
		yield $i;
	}

}
//===================================================================================

// $start=time();
// foreach (my_range(1,1000000000000000) as $n) {
// 	echo $n."\n";
// }
// $end=time();

// var_dump(my_range(1,2));
// echo "Use Time :".($end-$start)."s";

//===================================================================================

$i=100;
function gen(){
	$i=0;
	while(1){
		echo $i++;	
		echo "start"."<br/>";
		echo (yield ." middle"."<br/>");
		echo 'end'."<br/>";
	}

}

$gen=gen();
$gen->current();//output start
$gen->next();//output  middle end start  next执行到下一个yield

$gen->send("echo");

//===================================================================================

// function echoLogger(){
// 	while (true) {
// 		echo "Log :".yield."<br>";
// 	}
// }

// $log=echoLogger();

// $log->send('hello');//output hello
// $log->send('hello1');//output hello1
// $log->send('hello2');//output hello2
// $log->send('hello3');//output hello3
// $log->send('hello4');//output hello4
// $log->send('hello5');//output hello5
// $log->send('hello6');//output hello6
// $log->current();
//===================================================================================

// $start1=time();
// foreach (range(1, 1001110) as $n) {
// 	// echo $n."\n";
// }
// $end1=time();
// echo "Use Time :".($end1-$start1)."s";
