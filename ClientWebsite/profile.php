<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Profile',
	'title' => 'Profile'
);
$page = new PageBuilder($data);
$page->generate();
?>