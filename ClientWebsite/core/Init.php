<?php
class Init {
	public static function user(){
		$sql = "
			select * from
			user
			where
			username = 'robbin'
		";
		$db = DB::get();
		$result = $db->fetch_row($sql);
		if($result === false){
			$pid = DB_Person::add(array(
				'name'      => 'Robbin Harris',
				'email'     => 'spiderrobb@gmail.com',
				//'address'   => 'none',
				'phone'     => '3606218016',
				'gender'    => 'm',
				'birthdate' => '1989-05-24'
			));
			$uid = DB_User::add(array(
				'username'    => 'robbin',
				'password'    => 'admin',
				'permissions' => 'admin',
				'id_person'  => $pid
			));
			
		}
	}
}
?>