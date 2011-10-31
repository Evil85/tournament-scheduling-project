<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Users',
	'title' => 'User List'
);
$page = new PageBuilder($data);
$page->generate();
?>