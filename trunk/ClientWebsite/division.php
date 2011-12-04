<?php
require_once 'functions.php';
$data = array(
	'title' => 'Division',
	'page' => 'Page_Division'
);
$page = new PageBuilder($data);
$page->generate();
?>