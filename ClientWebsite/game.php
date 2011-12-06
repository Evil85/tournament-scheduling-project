<?php
require_once 'functions.php';
$data = array(
	'title' => 'Game',
	'page' => 'Page_Game'
);
$page = new PageBuilder($data);
$page->generate();
?>
