<?php
class Page_Signup implements Page_Interface{
	private $form;
	public function __construct(){
		$this->form = new Display_Form();
		$this->form->addTitle('Your Info');
		$this->form->addInput(array(
			'label' => 'Username',
			'require' => true,
			'name' => 'user[username]'
		));
		$this->form->addInput(array(
			'label' => 'Password',
			'require' => true,
			'name' => 'user[password]'
		));
		$this->form->addInput(array(
			'label' => 'Name',
			'require' => true,
			'name' => 'person[name]'
		));
		$this->form->addInput(array(
			'label' => 'Email',
			'require' => true,
			'name' => 'person[email]'
		));
		$this->form->addInput(array(
			'label' => 'phone',
			'require' => true,
			'name' => 'person[phone]'
		));
		$this->form->addDropDown(array(
			'label' => 'Gender',
			'require' => true,
			'name' => 'person[gender]',
			'options' => array(
				' - ' => 'none',
				'Male' => 'm',
				'Female' => 'f'
			)
		));
		$this->form->addInput(array(
			'label' => 'BirthDate',
			'require' => true,
			'name' => 'person[birthdate]',
			'id' => 'birthdate'
		));
		$this->form->addControl(array(
			'label' => 'Submit',
			'name' => 'signup_submit'
		));
		$this->validate();
		
		// adding style and script and style for page
		Style::get('jquery_datepicker');
		Script::get('jquery_datepicker');
		$script = '
			$(function() {
				$( "#birthdate" ).datepicker({ 
				yearRange: "1950:2012",
				dateFormat: "yy-mm-dd",
				changeMonth: true,
				changeYear: true
				});
			});
		';
		Script::add($script);
		$custom_style = "
			<style>
			#ui-datepicker-div{
				font-size:16px;
			}
			</style>
		";
		Style::add($custom_style);
	}
	private function validate(){
		if(isset($_POST['signup_submit'])){
			// code for adding a person
			$person = $_POST['person'];
			$allow = array('name','email','phone','gender','birthdate');
			$person = $this->form->getData($allow,$person);
			$pid = DB_Person::add($person);
			$user = $_POST['user'];
			$allow = array('username','password');
			$user = $this->form->getData($allow,$user);
			$user['pid_person'] = $pid;
			$user['permissions'] = 'user';
			$uid = DB_User::add($user);
			$_SESSION['uid'] = $uid;
			header('location: login.php');
		}
	}
	public function permissions(){
		return 'public';
	}
	public function generate(){
		?><h2>Signup!</h2><?php
		$style = 'width:400;margin:10px auto;';
		if(User::isMobile()){
			$style = 'margin:5px;';
		}
		?><div style="<?php echo $style; ?>"><?php
		$this->form->generate();
		?></div><?php
	}
}
?>