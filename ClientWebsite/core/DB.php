<?php
class DB {
	private static $dbs = array();
	
	private static function connect($name){
		if($name == 'racket_ball'){
			$host  = 'localhost';
			$user  = 'admtourn201140';
			$pass  = 'yinvamOph';
			$select_db =  'tourn_201140';
		} else {
			return false;
		}
		$db = new DBO();
		$check = $db->connect($host,$user,$pass);
		if($check === false){
			Error::add('DB','Could not connect to db');
			return false;
		}
		$check = $db->select_db($select_db);
		if($check === false){
			Error::add('DB','Could not select db');
			return false;
		}
		DB::$dbs[$name] = $db;
	}
	
	public static function get($name = 'racket_ball'){
		if(!array_key_exists($name,DB::$dbs)){
			DB::connect($name);
		}
		return DB::$dbs[$name];
	}
}
?>