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
			$sql = "
				select * from
				division
				where
				did = {$id}
			";
			self::$divisionData[$id] = $db->fetch_row($sql);
		}
		return self::$divisionData[$id];
	}
	
	// funciton gets a division list for a tournament
	private static $divisionList = false;
	public static function getDivisionList($skip,$get,$tid){
		if(self::$divisionList === false){
			$db = DB::get();
			$user = DB_User::getUserData();
			$pid  = $user['pid_person'];
			$sql = "
				select d.*, p.players, pl.plid from
				division d 
				left join (
					select count(*) as players, did_division as did from
					player
					where did_division = {$tid}
					group by did_division
				) as p
				on d.did = p.did
				left join (
					select plid, did_division as did from
					player
					where
					(pid_player1 = {$pid} or pid_player2 = {$pid})
					limit 1
				) pl
				on d.did = pl.did
				where
				tid_tournament = {$tid}
				order by did desc
				limit {$skip},{$get}
			";
			self::$divisionList = $db->fetch_all($sql);
		}
		return self::$divisionList;
	}
	
	// funciton gets the division count for a tournament
	private static $divisionCount = false;
	public static function getDivisionCount($tid){
		if(self::$divisionCount === false){
			$db = DB::get();
			$sql = "
				select count(*) as count from
				division
				where
				tid_tournament = {$tid}
			";
			$result = $db->fetch_row($sql);
			self::$divisionCount = $result['count'];
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
				did_division = {$tid} and
				(pid_player1 = {$player_id} or pid_player2 = {$player_id}) 
			";
			$result = $db->fetch_row($sql);
			self::$signed_up = $result['count'];
		}
		return self::$signed_up;
	}
}
?>