<?php
class Person {
	public static function add($data){
		$db = DB::get();
		return $db->insert('person',$data);
	}
}
?>