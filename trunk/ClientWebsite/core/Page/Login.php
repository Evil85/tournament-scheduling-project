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
		?><h2>Welcome</h2><?php
		$style = 'width:400;margin:10px auto;';
		if(User::isMobile()){
			$style = 'margin:5px;';
		}
		?><div style="<?php echo $style; ?>"><?php
		$this->login->generate();
		if(User::is_public()){
			?><div style="text-align:right;margin-top:15px;">Don't have an account? <?php
			?><a href="signup.php" title="Create an account!">Click Here</a><?php
			?></div><?php
		}
		?></div><?php
	}
}
?>