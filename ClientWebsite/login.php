<?php
require 'functions.php';
$data = array(
	'page'  => 'Page_Login',
	'title' => 'Login'
);
$page = new PageBuilder($data);
$page->generate();