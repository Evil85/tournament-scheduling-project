<?php
class DB_Court {
	// function adds court
	public static function add($data){
		$db = DB::get();
		return $db->insert('court',$data);
	}
	
	// function gets court data
	private static $courtData = array();
	public static function getCourtData($id){
		if(!isset(self::$courtData[$id])){
			$db = DB::get();
			$sql = "
				select * from
				court
				where
				cid = {$id}
			";
			self::$courtData[$id] = $db->fetch_row($sql);
		}
		return self::$courtData[$id];
	}
	
	// function gets court list for given location
	private static $courtList = false;
	public static function getCourtList($id){
		if(self::$courtList === false){
			$db = DB::get();
			$sql = "
				select * from
				court
				where
				lid_location = {$id}
				order by courtName
			";
			self::$courtList = $db->fetch_all($sql);
		}
		return self::$courtList;
	}
	
	// function gets court cournt for a given location
	private static $courtCount = false;
	public static function getCourtCount($id){
		if(self::$courtCount === false){
			$db = DB::get();
			$sql = "
				select count(*) as count from
				court
				where
				lid_location = {$id}
			";
			self::$courtCount = $db->fetch_row($sql);
		}
		return self::$courtCount;
	}
}
?>