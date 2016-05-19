<?php
namespace f;

class F {}

$f=new F;   #success

$f=new \f\F; #success

$f=new \F;   # Fatal error: Class 'F' not found
