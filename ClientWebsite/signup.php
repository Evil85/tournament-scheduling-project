<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Signup',
	'title' => 'Create Account'
);
$page = new PageBuilder($data);
$page->generate();
?>