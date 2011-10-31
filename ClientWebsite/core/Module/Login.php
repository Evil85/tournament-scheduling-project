<?php
class Module_Login implements Module_Interface {
	private $user_info;
	private $form;
	public function __construct($data = '') {
		if(isset($_POST['login_submit'])){
			User::login($_POST['username'],$_POST['password']);
		} else if(isset($_POST['logout_submit'])){
			User::logout();
		}
		$this->user_info = User::get_user_info();
		
		$style = "
		<style>
			.login_module{
				background-color:#eee;
				border-radius:5px;
				overflow:hidden;
				width:400px;
				margin-left:auto;
				margin-right:auto;
				margin-top:10px;
				margin-bottom:10px;
				padding:10px;
			}
		</style>
		";
		Style::add($style);
		
		// creating form
		$this->form = new Display_Form(array('method' => 'post'));
		if(User::is_public()){
			$this->form->addTitle('Login');
			$this->form->addInput(array(
				'label'   => 'Username',
				'require' => true,
				'name'    => 'username'
			));
			$this->form->addInput(array(
				'label'   => 'Password',
				'require' => true,
				'name'    => 'password',
				'type'    => 'password'
			));
			$this->form->addControl(array(
				'label' => 'login',
				'name'  => 'login_submit'
			));
		} else {
			$this->form->addTitle('Logout');
			$this->form->addSubText('Username: '.$this->user_info['username']);
			$this->form->addControl(array(
				'label' => 'logout',
				'name'  => 'logout_submit'
			));
		}
	}
	public function generate(){
		$this->form->generate();
	}
}
?>