<?php
class DB_Venue {
	// this function adds a venue
	public static function add($data){
		$db = DB::get();
		return $db->insert('venue',$data);
	}
}
?>