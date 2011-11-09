<?php
class DB_Foul {
	// this function adds a foul
	public static function add($data){
		$db = DB::get();
		return $db->insert('foul',$data);
	}
}
?>