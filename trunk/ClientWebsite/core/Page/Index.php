<?php
class Page_Index implements Page_Interface{
	private $t;
	private $p;
	private $t_count;
	public function __construct(){
		$this->t = new Display_Table();
		$perpage = 5;
		$count   = DB_Tournament::getTournamentCount();
		$this->p = new Display_Pager($perpage,$count);
		$this->tournaments = DB_Tournament::getTournamentList($this->p->skip,$perpage);
	}
	public function permissions(){
		return array('public');
	}
	public function generate(){
		?><h2>Current Tournaments<?php
		if($this->t_count > 0) {
			echo ' ('.$this->t_count.')';
		}
		?></h2><?php
		if(count($this->tournaments) > 0){
			?><div style="margin:10px;"><?php
			$this->p->generate();
			$t = $this->t;
			$t->newTable(array('width'=>'100%'));
			foreach($this->tournaments as $tour){
				$t->newRow();
				$t->newCol(array(
					'rowspan'=>3,
					'align'=>'center',
					'valign'=>'center'
				));
				?><a href="tournament.php?id=<?php echo $tour['tid']; ?>" title="View Tournament"><?php
				?><img src="style/images/tournament_icon.jpg" alt="tournament icon" /><?php
				?></a><?php
				$t->newCol();
				echo 'Name:';
				$t->newCol();
				echo $tour['name'];
				
				$t->newRow();
				$t->newCol();
				echo 'Start Date:';
				$t->newCol();
				echo $tour['start_date'];
				
				$t->newRow();
				$t->newCol();
				echo 'End Date:';
				$t->newCol();
				echo $tour['end_date'];
			}
			$t->end();
			$this->p->generate();
			?></div><?php
		}
	}
}
?>