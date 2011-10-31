<?php
class Page_Admin implements Page_Interface {
	private $user_form;
	private $tournament_form;
	
	public function __construct(){
		$this->user_form       = new Module_Add_User();
		$this->tournament_form = new Module_Add_Tournament();
	}
	public function permissions(){
		return array('admin');
	}
	public function generate(){
		?><h2>Admin Control Panel</h2><?php
		?><div style="margin:5px;"><?php
		$this->user_form->generate();
		$this->tournament_form->generate();
		?></div><?php
	}
}
?>