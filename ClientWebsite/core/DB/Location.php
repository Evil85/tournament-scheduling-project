<?php
class DB_Location {
	// this function adds a location
	public static function add($data){
		$db = DB::get();
		return $db->insert('location',$data);
	}
	
	// this function gets a locations data
	private static $locationData = array();
	public static function getLocationData($lid){
		if(!isset(self::$locationData[$lid])){
			/*
			$db = DB::get();
			$sql = "
				select * from
				location
				where 
				id = {$lid}
			";
			self::$locationData[$lid] = $db->fetch_row($sql);
			*/
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'location',
				'ID' => "{$lid}"
			);
			self::$locationData[$lid] = Socket::request($data);
		}
		return self::$locationData[$lid];
	}
	
}
?>
