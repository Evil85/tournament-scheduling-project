<?php
require_once 'functions.php';
$data = array(
	'page'  => 'Page_Bracket',
	'title' => 'Bracket'
);
$page = new PageBuilder($data);
$page->generate();
?>
