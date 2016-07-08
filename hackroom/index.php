<!DOCTYPE html><!--STATUS OK--><html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<script>
window.alert=function ()
{
confirm("pang!");
}
</script>
<title>XSS ME </title>
</head>
<body>
<h1 align=center>XSS ME </h1>
<?php
header("x-xss-Protection:0");
ini_set("display_errors", 0);
$str= $_GET["xss"];
$str2=str_replace("(","_",$str);
$str3=str_replace(")","_",$str2);
$str4=str_replace("&","_",$str3);
$str5=str_replace("\\","_",$str4);
$str6=str_replace("<","_",$str5);
$str7=str_replace("%28","_",$str6);
$str8=str_replace("%29","_",$str7);
$str9=str_replace(">","_",$str8);
$str10=str_replace("'","_",$str9);
$str11=str_replace(" on","_on",$str10);
$str12=str_replace("alert","_",$str11);
$str13=str_replace("innerHTML","_",$str12);
$str14=str_replace("document","_",$str13);
$str15=str_replace("appendChild","_",$str14);
$str16=str_replace("createElement","_",$str15);
$str17=str_replace("src","_",$str16);
$str18=str_replace("write","_",$str17);
$str19=str_replace("String","_",$str18);
$str20=str_replace("eval","_",$str19);
$str21=str_replace("setTimeout","_",$str20);
$str22=str_replace("unescape","_",$str21);
$str22=str_replace("data","da_ta",$str21);

echo '<center><form action=index.php method=GET>
<input name=xss  value="'.$str22.'">
<input type=submit name=submit value="Type" />
</form>
</center>';
?>
<center>
<?php
echo "<h3 align=center>payload length:".strlen($str22)."</h3>";
?>
</center>

</body>
</html>
