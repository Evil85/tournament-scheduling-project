<?php
class DB_Game {
	// this function adds a game
	public static function add($data){
		$db = DB::get();
		return $db->insert('game',$data);
	}
}
?>