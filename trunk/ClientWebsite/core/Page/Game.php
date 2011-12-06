<?php
class Page_Game implements Page_Interface{
	private $game = false;
	private $table;
	public function __construct($ajaxed = false){
		if(isset($_GET['id']) && is_numeric($_GET['id'])){
			$this->game = DB_Game::getGameData($_GET['id']);
			$this->match = DB_Match::getMatchData($this->game['id_match']);
			$this->player_1 = DB_Player::getPlayerData($this->match['id_player1']);
			$this->player_2 = DB_Player::getPlayerData($this->match['id_player2']);
			// getting team 1 info
			$this->team1 = array();
			$this->team1[] = DB_Person::getPersonData($this->player_1['id_player1']);
			if($this->player_1['id_player2'] != null){
				$this->team1[] = DB_Person::getPersonData($this->player_1['id_player2']);
			}
			// getting team 2 info
			$this->team2 = array();
			$this->team2[] = DB_Person::getPersonData($this->player_2['id_player1']);
			if($this->player_1['id_player2'] != null){
				$this->team2[] = DB_Person::getPersonData($this->player_2['id_player2']);
			}
			$this->table = new Display_Table();
			Script::get('display');
			// writing script for updating page automaticly
			if($ajaxed === false && !User::is_admin()){
				$time = 1000;
				if(User::isMobile()){
					$time = 3000;
				}
				$custom_script = "
					function updateLiveMatch(){
						loadPage('game_content','action.php?a=update&id={$_GET['id']}');
					}
					var live_update = setInterval('updateLiveMatch()',{$time});
				";
				Script::add($custom_script);
			}
			
			// writing custom css
			$hover = "
				.team1but:hover, .team2but:hover{
					background-color:#0f6;
				}
			";
			if(User::isMobile()){
				$hover = '';
			}
			$custom_style = "
				<style>
				.team1but, .team2but{
					border-style:solid;
					border-color:#ccc;
					border-width:2px;
					border-radius:10px;
					display:block;
					height:50px;
					width:150px;
					text-align:center;
					line-height:50px;
					text-decoration:none;
					color:#222;
					font-weight:bold;
					margin:auto auto;
				}
				.team1but{
					background-color:#09f;
				}
				.team2but{
					background-color:#c33;
				}
				{$hover}
				</style>
			";
			Style::add($custom_style);
		}
	}
	public function permissions(){
		if($this->game === false){
			return '';
		}
		return 'public';
	}
	public function generate($ajaxed = false){
		if($ajaxed === false){
			?><div id="game_content"><?php
		}
		?><h2>Game</h2><?php
		?><div style="margin:10px;"><?php
		if(!User::isMobile()){
			?><h2 style="text-align:center"><?php
			echo $this->team1[0]['name'];
			if(isset($this->team1[1])){
				echo ' and ';
				echo $this->team1[1]['name'];
			}
			?><br/>VS.<br/><?php
			echo $this->team2[0]['name'];
			if(isset($this->team2[1])){
				echo ' and ';
				echo $this->team2[1]['name'];
			}
			?></h2><?php
		}
		$t = $this->table;
		$t->newTable(array('width'=>'100%','class'=>''));
		$t->newCol(array('width'=>'50%','align'=>'center'));
		echo '1. '.$this->team1[0]['name'];
		if(isset($this->team1[1])){
			echo ' and ';
			echo $this->team1[1]['name'];
		}
		$t->newCol(array('rowspan'=>3,'align'=>'center'));
		echo 'VS.';
		$t->newCol(array('width'=>'50%','align'=>'center'));
		echo '2. '.$this->team2[0]['name'];
		if(isset($this->team2[1])){
			echo ' and ';
			echo $this->team2[1]['name'];
		}

		$t->newRow();
		$t->newCol(array('align'=>'center'));
		?><span id="team1_score" style="font-size:20px;">Score: <?php
		echo $this->game['team1Score'];
		?></span><?php

		$t->newCol(array('align'=>'center'));
		?><span id="team2_score" style="font-size:20px;">Score: <?php
		echo $this->game['team2Score'];
		?></span><?php
		// printing out 
		if($this->game['phase'] <= 1 && User::is_admin()){
			$t->newRow();
			$t->newCol();
			?><a href="#" class="team1but" onclick="loadPage('game_content','action.php?a=addpoint&id=<?php
			echo $this->game['id'];
			?>&team=1'); return false">Score</a><?php
			$t->newCol();
			?><a href="#" class="team2but" onclick="loadPage('game_content','action.php?a=addpoint&id=<?php
			echo $this->game['id'];
			?>&team=2'); return false">Score</a><?php
		}
		$t->end();
		?></div><?php
		if($ajaxed === false){
			?></div><?php
		}
		
	}
}
?>
