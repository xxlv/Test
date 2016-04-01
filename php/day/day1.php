<?php
// $data=['a'=>'A','b'=>'B','c'=>'C','d'=>'D'];
// $data2=['e'=>'A','f'=>'F','h'=>'D'];
// print_r(array_diff($data,$data2));

$data=['news_item'=>[
    ['title'=>1],
    ['title'=>2]
        ]];

var_dump(array_pop($data['news_item']));
