<?php
class Page_Match implements Page_Interface{
	private $login;
	public function __construct(){
		$this->login = new Module_Login();
	}
	public function permissions(){
		return array('public');
	}
	public function generate(){
		?>Matches<?php
		$this->login->generate();
	}
}

?>