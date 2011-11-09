<?php
class Module_Login implements Module_Interface {
	private $user_info;
	private $form;
	public function __construct($data = '') {
		// creating form
		$this->form = new Display_Form(array('method' => 'post'));
		
		if(isset($_POST['login_submit'])){
			$result = DB_User::login($_POST['username'],$_POST['password']);
			if($result === false){
				$this->form->setError('Incorrect Username or Password.');
				$this->form->failed();
			}
		} else if(isset($_POST['logout_submit'])){
			DB_User::logout();
		}
		$this->user_info = DB_User::getUserData();
		
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