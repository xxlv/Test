<?php
require_once './HttpClient.php';
$client=new  HttpClient();
//$client2=new  HttpClient();
//
//
//$client->setVersion('1.1');
//
//$headers   =[];
//$headers[] = 'Host: auth.dxy.cn';
//$headers[] = 'Method: POST';
//$headers[] = 'Path: /accounts/login?resource=pop&method=POST&service=http://www.dxy.cn&iurl=http://www.dxy.cn';
//$headers[] = 'Scheme: https';
//$headers[] = 'Version: HTTP/1.1';
//$headers[] = 'Origin: https://auth.dxy.cn';
//$headers[] = 'Referer: https://auth.dxy.cn/accounts/login?resource=pop&method=POST&service=http://www.dxy.cn&iurl=http://www.dxy.cn';
//$headers[] = 'Accept-language: zh-CN,zh;q=0.8,en;q=0.6';
//$headers[] = 'Cache-Control: no-cache';
//$headers[] = 'Content-Type: application/x-www-form-urlencoded; charset=utf-8';
//$headers[] = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.101 Chrome/45.0.2454.101 Safari/537.36';
//
//$client->setHeaders($headers);
//$pre_login=$client->get('https://auth.dxy.cn/accounts/login?null')->getWebpage();
//$pattern='/name="nlt" value="(.+?)"/is';
//$match=[];
//preg_match($pattern, $pre_login, $match);
//$nlt = $match[1];
//
//$post_data = [
//    'username'=>'ddxxyyss',
//    'password'=>'dd1xx2yy3',
//    'trys'=>0,
//    'nlt'=>$nlt,
//    '_eventId'=>'submit'
//];
//$login_url='https://auth.dxy.cn/accounts/login?resource=pop&method=POST&service=http://www.dxy.cn&iurl=http://www.dxy.cn';
//$loginPage=$client
//    ->login($login_url,$post_data)
//    ->getWebpage();
//
////$client2->setVersion('1.0');
////$myurl='http://i.dxy.cn/profile/ddxxyyss';
////$page=$client2
////    ->get($myurl)
////    ->getWebpage();


//$client->auth($url,[]);
//$client->get($url);

