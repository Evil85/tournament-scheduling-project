<?php
class Page_Tournament implements Page_Interface {
	private $table;
	private $info;
	private $divisions;
	private $admin;
	private $add_division;
	private $p;
	private $gender;
	private $count;
	public function __construct() {
		if(isset($_GET['id']) && is_numeric($_GET['id'])){
			$this->info = DB_Tournament::getTournamentData($_GET['id']);
			$this->add_division = new Module_Add_Division(array('tid' => $this->info['id']));
			$perpage = 5;
			$count   = DB_Division::getDivisionCount($_GET['id']);
			$this->count = $count;
			$this->p = new Display_Pager($perpage,$count);
			$this->divisions = DB_Division::getDivisionList($this->p->skip,$perpage,$_GET['id']);
			$user = DB_User::getUserData();
			$this->gender = $user['gender'];
		}
		$this->table = new Display_Table();
		
		$style = "
		<style>
			.join, .joined{
				text-align:center;
				font-weight:bold;
				font-size:14px;
				color:#00f;
				padding:10px;
				border-radius:5px;
				text-decoration:none;
			}
			.joined{
				background-color:#CCC;
			}
			.join:hover{
				background-color:#CCC;
			}
		</style>
		";
		Style::add($style);
	}
	public function permissions() {
		if(!isset($_GET['id']) || !is_numeric($_GET['id'])){
			return array();
		}
		if(User::is_public() && $this->info['isGuestViewable'] == 1){
			return array('public');
		}
		return array('admin');
	}
	public function generate() {
		?><h2><?php
		echo $this->info['name'];
		?></h2><div style="margin:5px;"><?php
		$t = $this->table;
		
		// this table is for layout only
		$t->newTable(array('class'=>'','width'=>'100%'));
		$t->newCol(array('align'=>'left'));

		// tounament dates table
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Tounament Dates';
		$t->newRow();
		$t->newCol();
		echo 'Start Date:';
		$t->newCol();
		echo $this->info['start_date'];
		$t->newRow();
		$t->newCol();
		echo 'End Date:';
		$t->newCol();
		echo $this->info['end_date'];
		$t->end();
		
		if(User::get_platform() == 'mobile'){
			$t->newRow();
		}
		$t->newCol(array('align'=>'center'));
		
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Week Day Times';
		$t->newRow();
		$t->newCol();
		echo 'Start Time:';
		$t->newCol();
		echo date("g:i a", strtotime($this->info['start_time_weekdays']));
		$t->newRow();
		$t->newCol();
		echo 'End Time:';
		$t->newCol();
		echo date("g:i a", strtotime($this->info['end_time_weekdays']));
		$t->end();
		
		if(User::get_platform() == 'mobile'){
			$t->newRow();
		}
		$t->newCol(array('align'=>'right'));
		
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Weekend Times';
		$t->newRow();
		$t->newCol();
		echo 'Start Time:';
		$t->newCol();
		echo date("g:i a", strtotime($this->info['start_time_weekends']));
		$t->newRow();
		$t->newCol();
		echo 'End Time:';
		$t->newCol();
		echo date("g:i a", strtotime($this->info['end_time_weekends']));
		$t->end();
		
		$t->end();
		
		?></div><?php
		?><h2><?php
		echo 'Divisions';
		if($this->count > 0){
			echo ' ('.$this->count.')';
		}
		?></h2><div style="margin:5px;"><?php
		$this->p->generate();
		if(User::get_platform() == 'computer'){
			$t->newTable(array('width'=>'100%'));
			$t->newTitle();
			$t->newCol(array('colspan'=>2, 'width'=>'120'));
			echo 'Gender Constraint';
			$t->newCol(array('colspan'=>3));
			echo 'Division Info';
		}
		Debug::add('div',$this->divisions);
		foreach($this->divisions as $div){
			if(User::get_platform() == 'mobile'){
				$t->newTable(array('width'=>'100%'));
				$t->newTitle();
				$t->newCol(array('colspan'=>3));
				echo 'Division Info';
			}
			$t->newRow();
			if(!User::isMobile()){
				if($div['genderConstraint'] == 'm'){
					$t->newCol(array('rowspan'=>4,'colspan'=>2,'align'=>'center','width'=>120));
					?><span class="male_icon"></span><?php
				} else if($div['genderConstraint'] == 'f'){
					$t->newCol(array('rowspan'=>4,'colspan'=>2,'align'=>'center','width'=>120));
					?><span class="female_icon"></span><?php
				} else if($div['genderConstraint'] == 'a'){
					$t->newCol(array('rowspan'=>4,'align'=>'center','width'=>60));
					?><span class="female_icon"></span><?php
					$t->newCol(array('rowspan'=>4,'align'=>'center','width'=>60));
					?><span class="male_icon"></span><?php
				}
			}
			$t->newCol();
			echo 'Name:';
			$t->newCol();
			echo $div['name'];
			if(User::isMobile()){
				$t->newCol(array('align'=>'center','rowspan'=>5));
			} else {
				$t->newCol(array('align'=>'center','rowspan'=>4));
			}
			if(User::is_public()){
				?><a class="join" href="signup.php" title="Sign Up!">SignUp</a><?php
			} else {
				if($div['genderConstraint'] != $this->gender && $div['genderConstraint'] != 'a'){
					?><span class="joined">Restricted</span><?php
				} else if(is_numeric($div['plid'])){
					?><span class="joined">Entered</span><?php
				} else {
					?><a class="join" href="join.php?did=<?php 
					echo $div['id'];
					?>" title="click to join division">Join</a><?php
				}
			}
			
			$t->newRow();
			$t->newCol();
			echo 'Type:';
			$t->newCol();
			echo $div['tournType'];
			$t->newRow();
			$t->newCol();
			echo 'Teams:';
			$t->newCol();
			if($div['isDouble']){
				echo 'Doubles';
			} else {
				echo 'Singles';
			}
			$t->newRow();
			$t->newCol();
			echo 'Signed Up:';
			$t->newCol();
			?><a href="division.php?did=<?php
			echo $div['id'];
			?>" title="View Players"><?php
			echo (is_numeric($div['players']) ? $div['players'] : 0);
			?></a><?php
			if(User::isMobile()){
				$t->newRow();
				$t->newCol();
				echo 'Gender:';
				$t->newCol();
				if($div['genderConstraint'] == 'm'){
					echo 'Male';
				} else if($div['genderConstraint'] == 'f'){
					echo 'Female';
				} else if($div['genderConstraint'] == 'a'){
					echo 'All';
				}
				$t->end();
			}
		}
		$t->end();
		$this->p->generate();
		?></div><?php
		if(DB_Tournament::isAdmin($this->info['id'])){
			?><h2>Admin Controls</h2><?php
			?><div style="margin:5px;"><?php
			$this->add_division->generate();
			?></div><?php
		}
	}
}
?>
