<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Location',
	'title' => 'Club'
);
$page = new PageBuilder($data);
$page->generate();
?>