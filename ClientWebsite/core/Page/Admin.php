<?php
class Page_Admin implements Page_Interface {
	private $user_form;
	private $tournament_form;
	private $location_form;
	private $locations;
	private $table;
	
	public function __construct(){
		$this->tournament_form = new Module_Add_Tournament();
		$this->location_form = new Module_Add_Location();
		$perpage = 5;
		$count   = DB_Location::getLocationCount();
		$this->count = $count;
		$this->p = new Display_Pager($perpage,$count);
		$this->locations = DB_Location::getLocationList($this->p->skip,$count);
		$this->table = new Display_Table();
	}
	public function permissions(){
		return array('admin');
	}
	public function generate(){
		?><h2>Admin Control Panel</h2><?php
		?><div style="margin:5px;"><?php
		$this->tournament_form->generate();
		$this->location_form->generate();
		?></div><?php
		
		if(count($this->locations) > 0){
			?><h2>Locations</h2><?php
			?><div style="margin:5px"><?php
			$this->p->generate();
			$t = $this->table;
			$t->newTable(array('width'=>'100%'));
			$t->newTitle();
			$t->newCol();
			echo 'Name';
			$t->newCol();
			echo 'Address';
			foreach($this->locations as $location){
				$t->newRow();
				$t->newCol();
				?><a href="location.php?lid=<?php
				echo $location['id'];
				?>" title="View Location Info"><?php
				echo $location['name'];
				?></a><?php
				$t->newCol();
				echo $location['address'].', '.$location['city'].', '.$location['state'];
			}
			$t->end();
			$this->p->generate();
			?></div><?php
		}
	}
}
?>