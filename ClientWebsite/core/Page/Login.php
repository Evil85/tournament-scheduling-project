<?php
class Page_Login implements Page_Interface{
	private $login;
	public function __construct(){
		$this->login = new Module_Login();
	}
	public function permissions(){
		return array('public');
	}
	public function generate(){
		$style = 'width:400;margin:10px auto;';
		if(User::get_platform() == 'mobile'){
			$style = 'margin:5px;';
		}
		?><div style="<?php echo $style; ?>"><?php
		$this->login->generate();
		?></div><?php
	}
}
?>