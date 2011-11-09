<?php
class DB_Person {
	// funciton adds a person
	public static function add($data){
		$db = DB::get();
		return $db->insert('person',$data);
	}
	
	// funciton gets a persons info
	private static $personData = array();
	public static function getPersonData($id){
		if(!isset(self::$personData[$id])){
			$db = DB::get();
			$sql = "
				select * from
				person
				where
				pid = {$id}
			";
			self::$personData[$id] = $db->fetch_row($sql);
		}
		return self::$personData[$id];
	}
	
	// function returns a list of persons
	private static $personList = false;
	public static function getPersonList($skip,$get){
		if(self::$personList === false){
			$db = DB::get();
			$sql = "
				select * from 
				person
				order by name
				limit {$skip},{$get}
			";
			self::$personList = $db->fetch_all($sql);
		}
		return self::$personList;
	}
	
	// function returns person count
	private static $personCount = false;
	public static function getPersonCount(){
		if(self::$personCount === false){
			$db = DB::get();
			$sql = "
				select count(*) as count from
				person
			";
			$result = $db->fetch_row($sql);
			self::$personCount = $result['count'];
		}
		return self::$personCount;
	}
}
?>