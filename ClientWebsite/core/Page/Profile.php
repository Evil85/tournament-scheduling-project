<?php
class Page_Profile implements Page_Interface{
	private $user;
	private $t;
	public function __construct(){
		$this->t = new Display_Table();
		if(isset($_GET['uid']) && is_numeric($_GET['uid'])){
			$uid = $_GET['uid'];
		} else {
			$uid = 0;
		}
		$this->user = User::get_user_info($uid);
	}
	public function permissions(){
		if(User::is_public()){
			return array();
		}
		if($this->user === false){
			return array();
		}
		return array('public');
	}
	public function generate(){
		?><h2>Profile</h2><?php
		
		$t = $this->t;
		$t->newTable(array('style' => 'margin:10px auto;'));
		$t->newTitle();
		$t->newCol(array('colspan'=>3));
		echo $this->user['username'];
	
		$t->newRow();
		if(User::get_platform() == 'computer'){
			if($this->user['gender'] == 'm'){
				$t->newCol(array('rowspan'=>4));
				?><span class="male_icon"></span><?php
			} else if($this->user['gender'] == 'f'){
				$t->newCol(array('rowspan'=>4));
				?><span class="female_icon"></span><?php
			}
		}
		$t->newCol();
		echo 'Name:';
		$t->newCol();
		echo $this->user['name'];
		
		$t->newRow();
		$t->newCol();
		echo 'Email:';
		$t->newCol();
		echo $this->user['email'];
		
		$t->newRow();
		$t->newCol();
		echo 'Phone:';
		$t->newCol();
		echo $this->user['phone'];
		
		if(User::get_platform() == 'mobile'){
			$t->newRow();
			$t->newCol();
			echo 'Gender:';
			$t->newCol();
			if($this->user['gender'] == 'm') {
				echo 'Male';
			} else if($this->user['gender'] == 'f') {
				echo 'Female';
			}
		}
		
		$t->newRow();
		$t->newCol();
		echo 'Join Date:';
		$t->newCol();
		echo $this->user['date_joined'];
		
		$t->end();
	}
}
?>