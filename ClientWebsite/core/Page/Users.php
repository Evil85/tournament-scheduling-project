<?php
class Page_Users implements Page_Interface{
	private $count;
	private $people;
	private $p;
	private $t;
	public function __construct(){
		// creating display table
		$this->t = new Display_Table();
		
		// creating pager
		$perpage = 10;
		$this->count = DB_Person::getPersonCount($_GET['id']);
		$this->p     = new Display_Pager($perpage,$this->count);
		
		// getting people list
		$this->people = DB_Person::getPersonList($this->p->skip,$perpage);
	}
	public function permissions(){
		return 'admin';
	}
	public function generate(){
		?><h2>All People<?php
		if($this->count > 0) {
			echo ' ('.$this->count.')';
		}
		?></h2><?php
		?><div style="margin:5px"><?php
		$this->p->generate();
		$t = $this->t;
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol();
		echo 'ID';
		$t->newCol();
		echo 'Name';
		$t->newCol();
		echo 'Email';
		$t->newCol();
		echo 'Phone';
		$t->newCol();
		echo 'Gender';
		foreach($this->people as $person){
			$t->newRow();
			$t->newCol();
			?><a href="profile.php?pid=<?php
			echo $person['id'];
			?>" title="View Profile"><?php
			echo $person['id'];
			?></a><?php
			$t->newCol();
			echo $person['name'];
			$t->newCol();
			echo $person['email'];
			$t->newCol();
			echo $person['phone'];
			$t->newCol();
			if($person['gender'] == 'm'){
				echo 'Male';
			} else if($person['gender'] == 'f'){
				echo 'Female';
			} else {
				echo 'Unknown';
			}
		}
		$t->end();
		$this->p->generate();
		?></div><?php
	}
}
?>