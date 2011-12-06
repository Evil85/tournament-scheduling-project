<?php
class DB_Tournament {
	// function adds tournament
	public static function add($data){
		$db = DB::get();
		return $db->insert('tournament',$data);
	}
	
	// function returns the possible tournament types
	public static function getTypes(){
		return array('round robin' => 'round robin');
	}
	
	// function gets info for tournament
	private static $tournamentData = array();
	public static function getTournamentData($id){
		if(!isset(self::$tournamentData[$id])){
			/*
			$db = DB::get();
			$sql = "
				select * from
				tournament
				where
				id = {$id}
			";
			self::$tournamentData[$id] = $db->fetch_row($sql);
			*/
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'tournament',
				'ID' => "{$id}"
			);
			self::$tournamentData[$id] = Socket::request($data);
			
		}
		return self::$tournamentData[$id];
	}
	
	// function gets a list of tournaments
	private static $tournamentList = false;
	public static function getTournamentList($skip,$get){
		if(self::$tournamentList === false){
			/*
			$db = DB::get();
			$sql = "
				select * from
				tournament
				where
				isGuestViewable = 1
				order by id desc
				limit {$skip},{$get}
			";
			self::$tournamentList = $db->fetch_all($sql);
			*/
			$data = array(
				'Command'  => 'getTableOrderLimitSpecify',
				'TableName' => 'tournament',
				'SpecColumn' => 'isGuestViewable',
				'SpecValue' => '1',
				'OrderColumn' => 'id',
				'SkipCount' => "{$skip}",
				'GetCount'  => "{$get}"
			);
			self::$tournamentList = Socket::request($data);
		}
		return self::$tournamentList;
	}
	
	// function get tournament count
	private static $tournamentCount = false;
	public static function getTournamentCount(){
		if(self::$tournamentCount === false){
			/*
			$db = DB::get();
			$sql = "
				select count(*) as count from
				tournament
				where
				isGuestViewable = 1
			";
			$result = $db->fetch_row($sql);
			self::$tournamentCount = $result['count'];
			*/
			$data = array(
				'Command'  => 'getCountByValue',
				'TableName' => 'tournament',
				'ColumnName' => 'isGuestViewable',
				'ColumnValue' => "1",
			);
			$result = Socket::request($data);
			self::$tournamentCount = $result['result'];
		}
		return self::$tournamentCount;
	}
	
	// function checks to see if user is admin of tournament
	public static function isAdmin($tid){
		$user       = DB_User::getUserData();
		$tournament = self::getTournamentData($tid);
		Debug::add('test',$user);
		Debug::add('test',$tournament);
		return ($user['id'] == $tournament['id_owner']);
	}
	
	// tournament scheduling
	public static function schedule($id){
		$data = array(
			'Command' => 'scheduleTournament',
			'TournamentID' => "{$id}"
		);
		return Socket::request($data);
	}
}
?>
