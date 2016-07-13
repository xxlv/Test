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
$str=str_replace("(","_",$str);
$str=str_replace(")","_",$str);
$str=str_replace("&","_",$str);
$str=str_replace("\\","_",$str);
$str=str_replace("<","_",$str);
$str=str_replace("%28","_",$str);
$str=str_replace("%29","_",$str);
$str=str_replace(">","_",$str);
$str=str_replace("'","_",$str);
$str=str_replace(" on","_on",$str);
$str=str_replace("alert","_",$str);
$str=str_replace("innerHTML","_",$str);
$str=str_replace("document","_",$str);
$str=str_replace("appendChild","_",$str);
$str=str_replace("createElement","_",$str);
$str=str_replace("src","_",$str);
$str=str_replace("write","_",$str);
$str=str_replace("String","_",$str);
$str=str_replace("eval","_",$str);
$str=str_replace("setTimeout","_",$str);
$str=str_replace("unescape","_",$str);
$str=str_replace("data","da_ta",$str);

echo '<center><form action=index.php method=GET>
<input name=xss  value="'.$str.'">
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
