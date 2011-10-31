<?php
require 'functions.php';
if(isset($_SESSION['uid'])){
	$_SESSION = array();
}
header('location: login.php');
?>