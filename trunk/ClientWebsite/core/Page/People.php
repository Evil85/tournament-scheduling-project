<?php
class Page_People implements Page_Interface{
	private $count;
	private $people;
	private $p;
	private $t;
	public function __construct(){
		// creating 
		$this->t = new Display_Table();
		$this->p = new Display_Pager();
		
		$this->count = Person::getPersonCount();
		$perpage = 10;
		$page = $this->p->getPage()-1;
		$skip = $page*$perpage;
		$limit = ceil($this->count/$perpage);
		$this->p->setLimit($limit);
		$this->people = Person::getPersonList($skip,$perpage);
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
			echo $person['pid'];
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