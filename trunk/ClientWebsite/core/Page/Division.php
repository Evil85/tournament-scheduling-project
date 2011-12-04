<?php
class Page_Division implements Page_Interface {
	private $division;
	private $player;
	private $count;
	private $p;
	private $t;
	public function __construct(){
		$did = $this->getDID();
		if($did !== false){
			$this->division = DB_Division::getDivisionData($did);
			$perpage = 10;
			$this->count   = DB_Player::getPlayerCount($did);
			$this->p       = new Display_Pager($perpage,$this->count);
			$this->players = DB_Player::getPlayerList($this->p->skip,$perpage,$did);
			$this->t       = new Display_Table();
		}
	}
	private function getDID(){
		if(isset($_GET['did']) && is_numeric($_GET['did'])){
			return $_GET['did'];
		}
		return false;
	}
	public function permissions(){
		if($this->getDID() === false){
			return '';
		}
		return 'public';
	}
	public function generate(){
		?><h2><?php
		echo $this->division['name'].' - ('.$this->count.')';
		?></h2><?php
		?><a href="tournament.php?id=<?php
		echo $this->division['id_tournament'];
		?>" title="back" style="padding:5px;"><?php
		echo 'Back to Tournament';
		?></a><?php
		?><div style="margin:5px;"><?php
		$this->p->generate();
		$t = $this->t;
		$t->newTable(array('width'=>'100%'));
		$t->newTitle();
		$t->newCol(array('align'=>'center'));
		echo 'ID';
		$t->newCol();
		echo 'Name';
		if($this->division['isDouble']){
			$t->newCol(array('align'=>'center'));
			echo 'ID 2';
			$t->newCol();
			echo 'Name 2';
		}
		foreach($this->players as $player){
			$t->newRow();
			// showing player1 info
			$p1 = DB_Person::getPersonData($player['id_player1']);
			$t->newCol(array('align'=>'center'));
			?><a href="profile.php?pid=<?php
			echo $p1['id'];
			?>" title="View Profile"><?php
			echo $p1['id'];
			?></a><?php
			$t->newCol();
			echo $p1['name'];
			if($this->division['isDouble']){
				// showing player2 info
				$p2 = DB_Person::getPersonData($player['id_player2']);
				$t->newCol(array('align'=>'center'));
				?><a href="profile.php?pid=<?php
				echo $p2['id'];
				?>" title="View Profile"><?php
				echo $p2['id'];
				?></a><?php
				$t->newCol();
				echo $p2['name'];
			}
		}
		$t->end();
		
		$this->p->generate();
		?></div><?php
	}
}
?>