<?php
class Page_Location implements Page_Interface {
	private $visible  = '';
	private $location = false;
	private $courts;
	private $table;
	private $form;
	public function __construct(){
		if(isset($_GET['lid']) && is_numeric($_GET['lid'])){
			$lid =	(int) $_GET['lid'];
			if(User::is_admin()){
				$this->form = new Module_Add_Court($lid);
			}
			$this->location = DB_Location::getLocationData($lid);
			if(!isset($this->location['result'])){
				$this->visible = 'public';
			}
			$this->table = new Display_Table();
			
			// getting list of courts
			$this->courts = DB_Court::getCourtList($lid);
		}
	}
	public function permissions(){
		return $this->visible;
	}
	public function generate(){
		?><h2><?php 
		echo $this->location['name'];
		?></h2><?php
		$t = $this->table;
		
		// containing table
		$t->newTable(array('class'=>'','width'=>'100%','style'=>'padding:5px'));
		$t->newCol(array('valign'=>'top','width'=>'33%','align'=>'center'));
		
		// address & contact table
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Address';
		
		$t->newRow();
		$t->newCol();
		echo 'Street:';
		$t->newCol();
		echo $this->location['address'];
		
		$t->newRow();
		$t->newCol();
		echo 'City:';
		$t->newCol();
		echo $this->location['city'];
		
		$t->newRow();
		$t->newCol();
		echo 'State:';
		$t->newCol();
		echo $this->location['state'];
		
		$t->newRow();
		$t->newCol();
		echo 'Zip:';
		$t->newCol();
		echo $this->location['zip'];
		
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Contact';
		
		$t->newRow();
		$t->newCol();
		echo 'Phone:';
		$t->newCol();
		echo $this->location['phone'];
		$t->end();
		
		// column for displaying hours
		if(User::isMobile()){
			$t->newRow();
		}
		$t->newCol(array('valign'=>'top','width'=>'33%','align'=>'center'));
		
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Weekday Hours';
		
		$t->newRow();
		$t->newCol();
		echo 'Open:';
		$t->newCol();
		echo date("g:i a", strtotime($this->location['weekdayOpenTime']));
		
		$t->newRow();
		$t->newCol();
		echo 'Close:';
		$t->newCol();
		echo date("g:i a", strtotime($this->location['weekdayCloseTime']));
		
		$t->newTitle();
		$t->newCol(array('colspan'=>2));
		echo 'Weekend Hours';
		
		$t->newRow();
		$t->newCol();
		echo 'Open:';
		$t->newCol();
		echo date("g:i a", strtotime($this->location['weekendOpenTime']));
		
		$t->newRow();
		$t->newCol();
		echo 'Close:';
		$t->newCol();
		echo date("g:i a", strtotime($this->location['weekendCloseTime']));
		$t->end();
		
		// column for displaying courts
		if(User::isMobile()){
			$t->newRow();
		}
		$t->newCol(array('valign'=>'top','width'=>'33%','align'=>'center'));
		
		if(count($this->courts) > 0){
			$t->newTable(array('width'=>'100%'));
			$t->newTitle();
			$t->newCol();
			echo 'Courts';
			foreach($this->courts as $court){
				$t->newRow();
				$t->newCol();
				echo $court['courtName'];
			}
			$t->end();
		} else {
			?><h2>Add Courts</h2><?php
		}
		$t->end();
		
		// printing out admin controls
		if(User::is_admin()){
			?><h2>Admin Controls</h2><?php
			?><div style="margin:5px;"><?php
			$this->form->generate();
			?></div><?php
		}
	}
}
?>
