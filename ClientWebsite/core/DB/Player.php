<?php
class DB_Player {
	// function adds a new player
	public static function add($data){
		$db = DB::get();
		return $db->insert('player',$data);
	}
	
	// this function gets a list of players in division
	private static $player_list = false;
	public static function getPlayerList($skip,$get,$did){
		if(self::$player_list === false){
			/*
			$db = DB::get();
			$sql = "
				select * from
				player
				where
				id_division = {$did}
				limit {$skip},{$get}
			";
			Debug::add('sql',$sql);
			self::$player_list = $db->fetch_all($sql);
			*/
			$data = array(
				'Command'  => 'getTableOrderLimitSpecify',
				'TableName' => 'player',
				'SpecColumn' => 'id_division',
				'SpecValue' => "{$did}",
				'SkipCount' => "{$skip}",
				'GetCount'  => "{$get}"
			);
			self::$player_list = Socket::request($data);
			Debug::add('send',$data);
			Debug::add('return',self::$player_list);
		}
		return self::$player_list;
	}
	
	// this function gets the count of players in a division
	private static $player_count = false;
	public static function getPlayerCount($did){
		if(self::$player_count === false){
			/*
			$db = DB::get();
			$sql = "
				select count(*) as count from
				player
				where
				id_division = {$did}
			";
			$result = $db->fetch_row($sql);
			self::$player_count = $result['count'];
			*/
			$data = array(
				'Command'  => 'getCountByValue',
				'TableName' => 'player',
				'ColumnName' => 'id_division',
				'ColumnValue' => "{$did}",
			);
			$result = Socket::request($data);
			self::$player_count = $result['result'];
		}
		return self::$player_count;
	}
	
}
?>
