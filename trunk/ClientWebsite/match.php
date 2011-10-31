<?php
require 'functions.php';
$page_data = array(
	'title' => 'Match',
	'page' => 'Page_Match'
);
$page = new PageBuilder($page_data);
$page->generate();
?>