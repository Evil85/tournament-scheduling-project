<?php
class Page_Tournament implements Page_Interface {
	private $table;
	private $info;
	private $divisions;
	private $venues;
	private $admin;
	private $add_division;
	private $p;
	private $gender;
	private $count;
	public function __construct() {
		if(isset($_GET['id']) && is_numeric($_GET['id'])){
			$this->info = DB_Tournament::getTournamentData($_GET['id']);
			$this->add_division = new Module_Add_Division(array('tid' => $_GET['id']));
			$this->add_venue    = new Module_Add_Venue($_GET['id']);
			$perpage = 5;
			$count   = DB_Division::getDivisionCount($_GET['id']);
			$this->count = $count;
			$this->p = new Display_Pager($perpage,$count);
			$this->divisions = DB_Division::getDivisionList($this->p->skip,$perpage,$_GET['id']);
			$user = DB_User::getUserData();
			$this->gender = $user['gender'];
			$this->venues = DB_Venue::getVenueList($_GET['id']);
			// gettin schedule button event
			if($this->info['phase'] < 4 && isset($_GET['schedule']) && User::is_admin()){
				DB_Tournament::schedule($_GET['id']);
				$this->info['phase'] = 4;
			}
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
		Script::get('display');
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
		echo $this->info['phase'];
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
		
		// printing out venues
		if(count($this->venues) > 0){
			?><h2>Venues</h2><?php
			?><div style="margin:5px;"><?php
			$t->newTable(array('width'=>'100%'));
			$t->newTitle();
			$t->newCol();
			echo 'Name';
			$t->newCol();
			echo 'Address';
			foreach($this->venues as $venue){
				$t->newRow();
				$t->newCol();
				?><a href="location.php?lid=<?php
				echo $venue['id_location'];
				?>" title="View Location"><?php
				echo $venue['name'];
				?></a><?php
				$t->newCol();
				echo $venue['address'].', '.$venue['city'].', '.$venue['state'];
			}
			$t->end();
			?></div><?php
		}
		
		// printing out divisions
		if($this->count > 0){
			?><h2><?php
			echo 'Divisions';
			echo ' ('.$this->count.')';
			?></h2><?php
			?><div style="margin:5px;"><?php
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
				if($this->info['phase'] == 4 || $this->info['phase'] == 3){
					?><span class="joined">Processing Schedule</span><?php
				} else if($this->info['phase'] >= 5){
					?><a class="join" href="Bracket.php?id=<?php
					echo $div['id'];
					?>" title="View Schedule">View Schedule</a><?php
				} else if(User::is_public()){
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
		}
		
		// admin panel
		if(DB_Tournament::isAdmin($this->info['id']) || User::is_admin()){
			?><h2>Admin Controls</h2><?php
			?><div style="margin:5px;"><?php
			$this->add_division->generate();
			$this->add_venue->generate();
			?></div><?php
			if($this->info['phase'] < 4){
				?><a href="tournament.php?id=<?php
				echo $this->info['id'];
				?>&schedule" class="sbut">Schedule Tournament</a><?php
			}
		}
	}
}
?>
