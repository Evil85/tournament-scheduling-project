<?php
require_once 'functions.php';
$data = array(
	'page' => 'Page_Tournament',
	'title'    => 'Tournament'
);
$page = new PageBuilder($data);
$page->generate();
?>