<?php
class Person{
	private $person_list = false;
	public function getPersonList(){
		if(self::$person_list === false){
			$db = DB::get();
			$sql = "
				select * from 
				person
			";
		}
	}
}
?>