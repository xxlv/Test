<?php

class Xencrypt{

/**
*对称加密算法 
*
*@param string $string 加密串
*@param string $key 秘钥
*@return string  
*
*/

/*可以将指定的字符替换成特殊字符,但必须保证唯*/
public static $key_map_tpl=array(
		'+'=>'|___|',
		'-'=>'|__-|',
		'*'=>'|_-_|',
		'/'=>'|-__|',
		'='=>'|_--|'
	);

public static function encrypt($string,$key){


	$str=base64_encode($string);//base64加密
	$str_arr=str_split($str);   //转换成数组
	$str_count=count($str_arr); //获取数组大小
	foreach(str_split($key) as $k=>$v){
		//应用秘钥
		$k<$str_count&&$str_arr[$k].=$v;		
	}
	$mapper=self::$key_map_tpl;

	$search=array_keys($mapper);
	$replace=array_values($mapper); 

	return str_replace($search, $replace, join('',$str_arr));	
}

public static function decrypt($en_str,$key){
	//解密过程，倒着来
	$mapper=self::$key_map_tpl;
	$replace=array_keys($mapper);
	$search=array_values($mapper);

	$str=str_replace($search,$replace, $en_str);

	$str_arr=str_split($str,2);
	$str_count=count($str_arr);

	foreach(str_split($key) as $k=>$v){
		$k<$str_count&&$str_arr[$k][1]==$v&&$str_arr[$k]=$str_arr[$k][0];
	}
	return base64_decode(join('', $str_arr));

}

}

/*
//demo 
$str='Hell';
echo Xencrypt::encrypt($str,'word');
$pass=Xencrypt::encrypt($str,'word');
echo '<br/>';
echo Xencrypt::decrypt($pass,'word');
*/
