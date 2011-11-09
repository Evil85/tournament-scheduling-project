<?php
class DB_Tournament {
	// function adds tournament
	public static function add($data){
		$db = DB::get();
		return $db->insert('tournament',$data);
	}
	
	// function gets info for tournament
	private static $tournamentData = array();
	public static function getTournamentData($id){
		if(!isset(self::$tournamentData[$id])){
			$db = DB::get();
			$sql = "
				select * from
				tournament
				where
				tid = {$id}
			";
			self::$tournamentData[$id] = $db->fetch_row($sql);
		}
		return self::$tournamentData[$id];
	}
	
	// function gets a list of tournaments
	private static $tournamentList = false;
	public static function getTournamentList($skip,$get){
		if(self::$tournamentList === false){
			$db = DB::get();
			$sql = "
				select * from
				tournament
				where
				isGuestViewable = 1
				order by tid desc
				limit {$skip},{$get}
			";
			self::$tournamentList = $db->fetch_all($sql);
		}
		return self::$tournamentList;
	}
	
	// function get tournament count
	private static $tournamentCount = false;
	public static function getTournamentCount(){
		if(self::$tournamentCount === false){
			$db = DB::get();
			$sql = "
				select count(*) as count from
				tournament
				where
				isGuestViewable = 1
			";
			$result = $db->fetch_row($sql);
			self::$tournamentCount = $result['count'];
		}
		return self::$tournamentCount;
	}
	
	// function checks to see if user is admin of tournament
	public static function isAdmin($tid){
		$user       = DB_User::getUserData();
		$tournament = self::getTournamentData($tid);
		return ($user['uid'] == $tournament['uid_owner']);
	}
}
?>