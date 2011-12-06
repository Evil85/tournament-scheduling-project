<?php
// this file is for getting scores from live games
require 'functions.php';

if(!isset($_GET['a'])){
	echo 'fail error: 0';
}

// code for updating scores
if($_GET['a'] == 'addpoint'){
	// checking if user is admin
	if(!User::is_admin()){
		echo 'fail error: 1';
		die();
	}

	// getting id of game to update
	if(!isset($_GET['id']) && !is_numeric($_GET['id'])){
		echo 'fail error: 2';
		die();
	}

	// checking if team is specified
	if(!isset($_GET['team']) && !is_numeric($_GET['team'])){
		echo 'fail error: 3';
		die();
	}
	if($_GET['team'] != 1 && $_GET['team'] != 2){
		echo 'fail error: 4';
		die();
	}

	// checking to see if game is not over
	$game = DB_Game::getGameData($_GET['id']);
	$score = $game["team{$_GET['team']}Score"]+1;
	DB_Game::updateScore($_GET['id'],$_GET['team'],$score);

	// getting game info
	$content = new Page_Game();
	$content->generate(true);

// code for updating a page
} else if($_GET['a'] == 'update'){
	// getting id of game to update
	if(!isset($_GET['id']) && !is_numeric($_GET['id'])){
		echo 'fail error: 5';
		die();
	}
	
	// getting game info
	$content = new Page_Game();
	$content->generate(true);
} else if($_GET['a'] == 'schedule'){
	// getting id of game to update
	if(!isset($_GET['id']) && !is_numeric($_GET['id'])){
		echo 'fail error: 5';
		die();
	}
	
}

