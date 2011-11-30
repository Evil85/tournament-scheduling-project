<?php
class DB_Division {
	// function adds a division
	public static function add($data){
		$db = DB::get();
		return $db->insert('division',$data);
	}
	
	// funciton gets division data
	private static $divisionData = array();
	public static function getDivisionData($id){
		if(!isset(self::$divisionData[$id])){
			$db = DB::get();
			$user = DB_User::getUserData();
			$pid  = $user['id_person'];
			$sql = "
				select d.*, p.id_player1 as pid1, p.id_player2 as pid2  from 
				division d 
				left join (
					select * from
					player
					where 
					id_division = {$id} and
					(id_player1 = {$pid} or id_player2 = {$pid})
				) as p
				on p.id_division = d.id
				where
				d.id = {$id}
			";
			self::$divisionData[$id] = $db->fetch_row($sql);
		}
		return self::$divisionData[$id];
	}
	
	// funciton gets a division list for a tournament
	private static $divisionList = false;
	public static function getDivisionList($skip,$get,$tid){
		if(self::$divisionList === false){
			$user = DB_User::getUserData();
			$pid  = $user['id_person'];
			/*
			$db = DB::get();
			$sql = "
				select d.*, p.players, pl.plid from
				division d
				left join (
					select count(*) as players, id_division as did from
					player
					where 
					id_division in (
						select id from
						division 
						where
						id_tournament = {$tid}
					)
					group by did
				) as p
				on p.did = d.id
				left join ( 
					select id as plid, id_division as did from 
					player where 
					(id_player1 = {$pid} or id_player2 = {$pid}) 
				) pl on d.id = pl.did 
				where 
				d.id_tournament = {$tid}
				order by d.id desc
				limit {$skip},{$get}
			";
			Debug::add('sql',$sql);
			self::$divisionList = $db->fetch_all($sql);
			*/
			$data = array(
				'Command'  => 'getDivisionListForTourn',
				'TournamentID' => "{$tid}",
				'PlayerID' => "{$pid}",
				'SkipCount' => "{$skip}",
				'GetCount' => "{$get}"
			);
			self::$divisionList = Socket::request($data);
		}
		return self::$divisionList;
	}
	
	// funciton gets the division count for a tournament
	private static $divisionCount = false;
	public static function getDivisionCount($tid){
		if(self::$divisionCount === false){
			/*
			$db = DB::get();
			$sql = "
				select count(*) as count from
				division
				where
				id_tournament = {$tid}
			";
			$result = $db->fetch_row($sql);
			self::$divisionCount = $result['count'];
			*/
			$data = array(
				'Command'  => 'getCountByValue',
				'TableName' => 'division',
				'ColumnName' => 'id_tournament',
				'ColumnValue' => "{$tid}",
			);
			$result = Socket::request($data);
			self::$divisionCount = $result['result'];
		}
		return self::$divisionCount;
	}
	
	// function gets the number of divisions he has signed up for in tournament
	private static $signed_up = false;
	public static function signedUpFor($tid){
		if(self::$signed_up === false){
			$db = DB::get();
			$user = DB_User::getUserData();
			$player_id = $user['pid_person'];
			$sql = "
				select count(*) as count from
				player
				where 
				id_division = {$tid} and
				(id_player1 = {$player_id} or id_player2 = {$player_id}) 
			";
			$result = $db->fetch_row($sql);
			self::$signed_up = $result['count'];
		}
		return self::$signed_up;
	}
}
?>
