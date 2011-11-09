<?php
class DB_Venues {
	// this function adds a venues
	public static function add($data){
		$db = DB::get();
		return $db->insert('venues',$data);
	}
}
?>