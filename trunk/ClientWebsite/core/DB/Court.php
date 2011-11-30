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
			/*
			$db = DB::get();
			$sql = "
				select * from
				court
				where
				id = {$id}
			";
			self::$courtData[$id] = $db->fetch_row($sql);
			*/
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'court',
				'ID' => "{$id}"
			);
			self::$courtData[$id] = Socket::request($data);
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
				id_location = {$id}
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
			/*
			$db = DB::get();
			$sql = "
				select count(*) as count from
				court
				where
				id_location = {$id}
			";
			self::$courtCount = $db->fetch_row($sql);
			*/
			$data = array(
				'Command'  => 'getCountByValue',
				'TableName' => 'court',
				'ColumnName' => 'id_location',
				'ColumnValue' => "{$id}",
			);
			$result = Socket::request($data);
			self::$courtCount = $result['result'];
		}
		return self::$courtCount;
	}
}
?>
