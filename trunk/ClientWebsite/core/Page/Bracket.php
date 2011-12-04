<?php
class Page_Bracket implements Page_Interface {
	public $tourn = false;
	public $div   = false;
	public $data  = false;
	public $table = false;
	public function __construct(){
		if(isset($_GET['id']) && is_numeric($_GET['id'])){
			$this->data  = DB_Match::getMatches($_GET['id']);
			$this->div   = DB_Division::getDivisionData($_GET['id']);
			$this->tourn = DB_Tournament::getTournamentData($this->div['id_tournament']);
			$this->table = new Display_Table();
		}
	}
	public function permissions(){
		if($this->data === false){
			return '';
		}
		return 'public';
	}
	private function getXY(){
		$count = 0;
		$id = $this->data[0]['id_player1'];
		foreach($this->data as $match){
			if($match['id_player1'] == $id || $match['id_player2'] == $id){
				$count++;
			}
		}
		return $count;
	}
	public function generate(){
		?><h2><?php
		echo $this->tourn['name'].' - '.$this->div['name'];
		?></h2><?php
		//$count = count($this->data);
		$dim = $this->getXY();
		$t = $this->table;
		$i = 0;
		$j = 1;
		$prev = array();
		foreach($this->data as $match){
			if(!in_array($match['id_player1'],$prev)){
				$t->newCol(array('align'=>'center'));
				echo $match['id_player1'];
				$prev[] = $match['id_player1'];
			}
		}
		$t->newCol();
		// empty cell
		$t->newRow();
		foreach($this->data as $match){
			$t->newCol();
			echo $match['matchNumber'];
			?><br/><?php
			echo $match['id_player1'].' - '.$match['id_player2'];
			?><br/><?php
			echo date('l jS\,', strtotime($match['startTime']));
			?><br/><?php
			echo date('F Y\,', strtotime($match['startTime']));
			?><br/><?php
			echo date('g:i a', strtotime($match['startTime']));
			$i++;
			if($i == $j){
				// table fillers
				for($f=$i;$f<$dim;$f++){
					$t->newCol();
				}
				$t->newCol();
				echo $match['id_player2'];
				$t->newRow();
				$i = 0;
				$j++;
			}
		}
		$t->end();
	}
}
?>
