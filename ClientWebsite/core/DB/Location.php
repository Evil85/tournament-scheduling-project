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
	
	// this function returns a list of locations
	public static function getLocationList($skip,$get){
		$data = array(
			'Command'  => 'getTableOrderLimitSpecify',
			'TableName' => 'location',
			'OrderColumn' => 'name',
			'SkipCount' => "{$skip}",
			'GetCount'  => "{$get}"
		);
		return Socket::request($data);
	}
	
	// get list of all locations
	public static function getAllLocations(){
		$data = array(
			'Command'  => 'getTableOrderLimitSpecify',
			'TableName' => 'location',
			'OrderColumn' => 'name'
		);
		return Socket::request($data);
	}
	
	// gets the location count
	public static function getLocationCount(){
		$data = array(
			'Command'  => 'getTableCountByName',
			'TableName' => 'location'
		);
		$result = Socket::request($data);
		return $result['result'];
	}
	
}
?>
