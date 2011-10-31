<?php
require_once 'functions.php';
$data = array(
	'page'  => 'Page_Admin',
	'title' => 'Admin Controls'
);
$page = new PageBuilder($data);
$page->generate();
?>