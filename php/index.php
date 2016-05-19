<?php


$o=new Stdclass();
$o->hello='word';
$o=(array)$o;

echo $o['hello'];
exit;
//



header('Content-Type: image/png');


$frame = [];
for ($i = 0; $i <= 20; $i++) {
    for ($j = 0; $j <= 20; $j++) {
        $frame[$i][$j] = rand(0, 1);
    }
}

$x = 21 + 2 * $outerFrame + 100;
$y = 21 + 2 * $outerFrame + 100;


$offset=4;

$base_image = imagecreate($x, $y);
$white = imagecolorallocate($base_image, 255, 255, 255);
$black = imagecolorallocate($base_image, 0, 0, 0);

for ($i = 0; $i <= $x; $i++) {
    for ($j = 0; $j <= $y; $j++) {
        if($frame[$i][$j]==1){
            imagesetpixel($base_image, $j+$offset, $i+$offset, $black);
        }
    }
}


imagepng($base_image);
exit;


$outerFrame = 41;
$pixelPerPoint = 4;

$h = count($frame);
$w = count($frame[0]);


$imgW = $w + 2 * $outerFrame + 100;
$imgH = $h + 2 * $outerFrame + 100;

$base_image = imagecreate($imgW, $imgH);

$col[0] = imagecolorallocate($base_image, 255, 255, 255);
$col[1] = imagecolorallocate($base_image, 0, 0, 0);

imagefill($base_image, 0, 0, $col[0]);

for ($y = 0; $y < $h; $y++) {
    for ($x = 0; $x < $w; $x++) {
        if ($frame[$y][$x] == '1') {
            imagesetpixel($base_image, $x + $outerFrame, $y + $outerFrame, $col[1]);
        }
    }
}

$target_image = imagecreate($imgW * $pixelPerPoint, $imgH * $pixelPerPoint);
imagecopyresized($target_image, $base_image, 0, 0, 0, 0, $imgW * $pixelPerPoint, $imgH * $pixelPerPoint, $imgW, $imgH);
imagepng($target_image);
imagedestroy($base_image);
