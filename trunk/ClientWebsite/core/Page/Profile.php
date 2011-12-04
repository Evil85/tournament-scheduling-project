<?php
class Page_Profile implements Page_Interface{
	private $person;
	private $club;
	private $t;
	public function __construct(){
		$this->t = new Display_Table();
		if(isset($_GET['pid']) && is_numeric($_GET['pid'])){
			$pid = $_GET['pid'];
		} else {
			$pid = 0;
		}
		$this->person = DB_Person::getPersonData($pid);
		if($this->person['id_homeClub'] != null){
			$this->club = DB_Location::getLocationData($this->person['id_homeClub']);
		} else {
			$this->club = false;
		}
	}
	public function permissions(){
		if(User::is_public()){
			return array();
		}
		if($this->person === false){
			return array();
		}
		return array('public');
	}
	public function generate(){
		?><h2><?php
		echo $this->person['name'];
		?></h2><?php
		$t = $this->t;
		$t->newTable(array('style' => 'margin:10px auto;'));
		$t->newRow();
		if(!User::isMobile()){
			if($this->person['gender'] == 'm'){
				$t->newCol(array('rowspan'=>3));
				?><span class="male_icon"></span><?php
			} else if($this->person['gender'] == 'f'){
				$t->newCol(array('rowspan'=>3));
				?><span class="female_icon"></span><?php
			}
		}
		$t->newCol();
		echo 'Email:';
		$t->newCol();
		echo $this->person['email'];
		$t->newRow();
		$t->newCol();
		echo 'Phone:';
		$t->newCol();
		echo $this->person['phone'];
		if(User::isMobile()){
			$t->newRow();
			$t->newCol();
			echo 'Gender:';
			$t->newCol();
			if($this->person['gender'] == 'm') {
				echo 'Male';
			} else if($this->person['gender'] == 'f') {
				echo 'Female';
			} else {
				echo 'unknown';
			}
		}
		$t->newRow();
		$t->newCol();
		echo 'Birthday:';
		$t->newCol();
		echo $this->person['birthdate'];
		$t->end();
		
		// home club info
		if($this->club !== false){
			?><h2>Home Club</h2><?php
			$t->newTable(array('style' => 'margin:10px auto;'));
			$t->newTitle();
			$t->newCol(array('colspan'=>2));
			echo $this->club['name'];
			$t->newRow();
			$t->newCol();
			echo 'Address:';
			$t->newCol();
			echo $this->club['address'];
			$t->newRow();
			$t->newCol();
			echo 'City:';
			$t->newCol();
			echo $this->club['city'];
			$t->newRow();
			$t->newCol();
			echo 'State:';
			$t->newCol();
			echo $this->club['state'];
			$t->newRow();
			$t->newCol();
			echo 'Zip:';
			$t->newCol();
			echo $this->club['zip'];
			$t->newRow();
			$t->newCol();
			echo 'Phone:';
			$t->newCol();
			echo $this->club['phone'];
			$t->end();
		}
	}
}
?>