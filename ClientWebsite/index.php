<?php
require_once 'functions.php';
$data = array(
	'page' => 'Page_Index',
	'title'    => 'Home'
);
$page = new PageBuilder($data);
$page->generate();
?>
