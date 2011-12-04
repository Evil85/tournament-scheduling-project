<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Join',
	'title' => 'Join'
);
$page = new PageBuilder($data);
$page->generate();
?>