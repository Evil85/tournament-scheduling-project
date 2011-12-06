<?php
class DB_Game {
	// this function adds a game
	public static function add($data){
		$db = DB::get();
		return $db->insert('game',$data);
	}

	// this function gets a game data
	private static $gameData = array();
	public static function getGameData($id){
		if(!isset(self::$gameData[$id])){
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'game',
				'ID' => "{$id}"
			);
			self::$gameData[$lid] = Socket::request($data);
		}
		return self::$gameData[$lid];
	}
	public static function updateScore($id,$team,$value){
		unset(self::$gameData[$id]);
		$data = array(
			'Command' => 'updateGameScore',
			'GameID' => "{$id}",
			"Player{$team}Score" => "{$value}"
		);
		return Socket::request($data);
	}
}
?>
