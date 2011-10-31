<?php
class Module_Add_User implements Module_Interface{
	private $form;
	public function __construct($data = ''){
		$result = Permissions::getPermissions();
		$options = array();
		foreach($result as $option){
			$options[$option] = $option;
		}
		// building form
		$this->form = new Display_Form(array('method' => 'post'));
		$this->form->addTitle('Add User');
		$this->form->toggle(true);
		$this->form->addInput(array(
			'label'   => 'Username:',
			'name'    => 'username',
			'require' => true
		));
		$this->form->addInput(array(
			'label'   => 'Name:',
			'name'    => 'name',
			'require' => true
		));
		$this->form->addInput(array(
			'label'   => 'Password:',
			'name'    => 'password',
			'type'    => 'password',
			'require' => true
		));
		$this->form->addDropDown(array(
			'label'   => 'Role:',
			'name'    => 'permission',
			'require' => true,
			'options' => $options
		));
		$this->form->addControl(array(
			'label' => 'Add',
			'name'  => 'add_user_submit'
		));
		
		// validating form
		if(isset($_POST['add_user_submit'])){
			/*
			$data = array(
				'username' => $_POST['username'],
				'name'     => $_POST['name'],
				'password' => $_POST['password'],
				'role_id'  => $_POST['permission']
			);
			User::add($data);
			*/
		}
		// validating form
		/*
		if(isset($_POST['add_tournament'])){
			$allow = array(
				'username','password','permissions'
			);
			$data = $this->form->getData($allow);
			$user_info = User::get_user_info();
			$data['uid_owner'] = $user_info['uid'];
			$data['start_time_weekdays'] .= ':00:00';
			$data['end_time_weekdays']   .= ':00:00';
			$data['start_time_weekends'] .= ':00:00';
			$data['end_time_weekends']   .= ':00:00';
			
			$result = Tournament::add($data);
			if($result === false){
				// if form was not filled out correctly
				$this->form->failed();
			} else {
				$this->form->reset(true);
				Script::add("window.location = 'tournament.php?id={$result}';");
			}
		}
		*/
	}
	public function generate(){
		$this->form->generate();
	}
}