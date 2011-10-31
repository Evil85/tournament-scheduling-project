<?php
session_start();
function __autoload($class){
	$path = 'core/'.implode('/',explode('_',$class)).'.php';
	if(file_exists($path)){
		require_once($path);
	}
}
Init::user();
?>