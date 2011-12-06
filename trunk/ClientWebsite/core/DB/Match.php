<?php
class DB_Match {
	// this function adds a match
	public static function add($data){
		$db = DB::get();
		return $db->insert('match',$data);
	}
	
	public static function getMatches($id){
		$data = array(
			'Command'  => 'getMatchInfo',
			'DivisionID' => "{$id}"
		);
		$temp = Socket::request($data);
		Debug::add('socket',$temp);
		return $temp;
	}

	// this function gets a match data
	private static $matchData = array();
	public static function getMatchData($id){
		if(!isset(self::$matchData[$id])){
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'match',
				'ID' => "{$id}"
			);
			self::$matchData[$lid] = Socket::request($data);
		}
		return self::$matchData[$lid];
	}
}
?>
