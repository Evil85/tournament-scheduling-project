<?php
class Page_Join implements Page_Interface {
	private $division;
	private $tournament;
	private $form;
	private $referer;
	public function __construct() {
		if(isset($_GET['did']) && is_numeric($_GET['did'])){
			// getting all division and tournament info
			$this->division   = DB_Division::getDivisionData($_GET['did']);
			$this->tournament = DB_Tournament::getTournamentData($this->division['id_tournament']);
			
			// getting referer page
			if(isset($_POST['referer'])){
				$this->referer = $_POST['referer'];
			} else {
				$this->referer = $_SERVER['HTTP_REFERER'];
			}
			
			// building the form
			if(is_numeric($this->division['pid1']) || is_numeric($this->division['pid1'])) {
				// they have already entered
				$this->form = new Display_Form();
				$this->form->addTitle($this->division['name']);
				$this->form->addSubText('You have already entered this division.');
				$this->form->addControl(array(
					'label' => 'Back to Tournament',
					'onclick' => "window.location = 'tournament.php?id={$this->division['id_tournament']}';return false;"
				));
			} else if(!DB_Division::allowAnotherSignup($this->division['id_tournament'])) {
				// they have entered to many divisions
				$this->form = new Display_Form();
				$this->form->addTitle($this->division['name']);
				$this->form->addSubText('You have Entered to many divisions.');
				$this->form->addControl(array(
					'label' => 'Back to Tournament',
					'onclick' => "window.location = 'tournament.php?id={$this->division['id_tournament']}';return false;"
				));

			} else {
				$this->form = new Display_Form();
				$this->form->addTitle($this->tournament['name']);
				$this->form->addSubText('Division: '.$this->division['name']);
				$this->form->addInput(array(
					'type' => 'hidden',
					'name' => 'referer',
					'value' => $this->referer
				));
				if(!$this->division['isDouble']){
					$this->form->addControl(array(
						'label' => 'Join',
						'name'  => 'join_division'
					));
				} else {
					$this->form->setError('Double matches are currently unable to join');
				}
				$this->form->addControl(array(
					'label' => 'Cancel',
					'onclick' => "window.location = '{$this->referer}';return false;"
				));
				
				// validating form
				if($this->validate() == false){
					$this->form->failed();
				}
			}
			Debug::add('user',DB_User::getUserData());
		}
	}
	private function validate(){
		// validating form
		if(isset($_POST['join_division'])){
			// you cannot enter if you are already entered
			if($this->division['pid1'] != null || $this->division['pid2'] != null){
				$this->form->setError('You have already been entered in this division.');
				return false;
			}
			// you cannot enter if you do not meet gender constraints
			$user = DB_User::getUserData();
			if($this->division['genderConstraint'] != 'a' && $this->division['genderConstraint'] != $user['gender']){
				$this->form->setError('You do not meet gender constraints.');
				return false;
			}
			// 1. need to check age constraints
			// 2. need to check number of divisions entered constraint
			
			// you pass all constraints, entering you in contest
			$data = array(
				'id_player1'  => $user['id_person'],
				'id_division' => $_GET['did']
			);
			DB_Player::add($data);
			
			$this->form = new Display_Form();
			$this->form->addTitle($this->division['name']);
			$this->form->addSubText('You have successfully entered this division.');
			$this->form->addControl(array(
				'label' => 'Back to Tournament',
				'onclick' => "window.location = '{$this->referer}';return false;"
			));
		}
		return true;
	}
	public function permissions() {
		if(!isset($_GET['did']) || !is_numeric($_GET['did'])){
			return '';
		}
		return array('user','admin');
	}
	public function generate() {
		?><h2>Join</h2><?php
		
		if(User::isMobile()){
			?><div style="margin:5px"><?php
		} else {
			?><div style="width:400px;margin:10px auto"><?php
		}
		$this->form->generate();
		?></div><?php
	}
}
?>
