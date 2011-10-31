<?php
class Page_NoPermissions implements Page_Interface{
	public function __construct(){
	}
	public function permissions(){
		return array('public');
	}
	public function generate(){
		?><h2>You do not have permission to see this page.</h2><?php
	}
}
?>