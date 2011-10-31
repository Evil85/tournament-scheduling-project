<?php
class Page_Tournament implements Page_Interface {
	private $table;
	private $info;
	private $divisions;
	private $admin;
	private $add_division;
	public function __construct() {
		$this->info = false;
		if(isset($_GET['id']) && is_numeric($_GET['id'])){
			$this->info = Tournament::getTournamentInfo($_GET['id']);
			$this->add_division = new Module_Add_Division(array('tid' => $this->info['tid']));
			$this->divisions = Tournament::getDivisions($_GET['id']);
		}
		$this->table = new Display_Table();
	}
	public function permissions() {
		if($this->info === false){
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
		
		?></div><h2><?php
		echo 'Divisions';
		?></h2><div style="margin:5px;"><?php
		if(User::get_platform() == 'computer'){
			$t->newTable(array('width'=>'100%'));
			$t->newTitle();
			$t->newCol(array('colspan'=>2));
			echo 'Gender Constraint';
			$t->newCol(array('colspan'=>2));
			echo 'Division Info';
		}
		
		foreach($this->divisions as $div){
			if(User::get_platform() == 'mobile'){
				$t->newTable(array('width'=>'100%'));
				$t->newTitle();
				$t->newCol(array('colspan'=>2));
				echo 'Division Info';
			}
			$t->newRow();
			if(User::get_platform() == 'computer'){
				if($div['genderConstraint'] == 'm'){
					$t->newCol(array('rowspan'=>3,'colspan'=>2,'align'=>'center','width'=>120));
					?><span class="male_icon"></span><?php
				} else if($div['genderConstraint'] == 'f'){
					$t->newCol(array('rowspan'=>3,'colspan'=>2,'align'=>'center','width'=>120));
					?><span class="female_icon"></span><?php
				} else if($div['genderConstraint'] == 'a'){
					$t->newCol(array('rowspan'=>3,'align'=>'center','width'=>60));
					?><span class="female_icon"></span><?php
					$t->newCol(array('rowspan'=>3,'align'=>'center','width'=>60));
					?><span class="male_icon"></span><?php
				}
			}
			$t->newCol();
			echo 'Name:';
			$t->newCol();
			echo $div['name'];
			$t->newRow();
			$t->newCol();
			echo 'Type:';
			$t->newCol();
			echo $div['tournType'];
			$t->newRow();
			$t->newCol();
			echo 'Players:';
			$t->newCol();
			if($div['isDouble']){
				echo 'Doubles';
			} else {
				echo 'Singles';
			}
			if(User::get_platform() == 'mobile'){
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
				?><br/><?php
			}
		}
		$t->end();
		?></div><?php
		if(Tournament::is_admin($this->info['tid'])){
			?><h2>Admin Controls</h2><?php
			?><div style="margin:5px;"><?php
			$this->add_division->generate();
			?></div><?php
		}
	}
}
?>