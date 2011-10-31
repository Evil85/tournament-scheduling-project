<?php
class PageBuilder{
	private $header;
	private $page;
	private $title;
	public function __construct($data = array()){
		if(!isset($data['page'])){
			$data['page'] = 'Page_Index';
		}
		if(!isset($data['title'])){
			$data['title'] = 'Home';
		}
		$this->page = new $data['page']();
		if(!Permissions::can_see($this->page->permissions())) {
			$this->page = new Page_NoPermissions();
		}
		$this->title  = $data['title']; 
		$this->header = new Module_Header();
		Style::get('page');
		Debug::add('session',$_SESSION);
	}
	public function generate(){
		?><html><?php
		?><head><?php
		?><title><?php echo $this->title; ?></title><?php
		if(User::get_platform() == 'mobile'){
			?><meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no"><?php
		}
		Style::generate(); 
		?></head><?php
		?><body><?php
		$this->header->generate();
		?><div class="main_content"><?php
		$this->page->generate();
		?></div><?php
		$platform = new Module_ChangePlatform();
		$platform->generate();
		Debug::generate();
		Script::generate();
		?></body><?php
		?></html><?php
	}
}
?>