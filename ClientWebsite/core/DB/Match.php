<?php
class DB_Match {
	// this function adds a match
	public static function add($data){
		$db = DB::get();
		return $db->insert('match',$data);
	}
}
?>