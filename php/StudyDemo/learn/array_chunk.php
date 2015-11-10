<?php


$a=['hello'=>'world','1','2',1,2,2,2,3,4,4,5,5,6,6,7,7,8,7,5,4];
print_r($a);


echo PHP_EOL;

print_r(array_chunk($a,2,true));


