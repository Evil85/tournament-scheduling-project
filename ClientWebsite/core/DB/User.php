<?php
class DB_User {
	// function for logging in user
	public static function login($username,$password){
		$db = DB::get();
		$sql = "
			select * from
			user
			where
			strcmp(username,'{$username}') = 0 and
			strcmp(password,'{$password}') = 0
		";
		$result = $db->fetch_row($sql);
		if($result === false){
			return false;
		}
		$_SESSION['uid'] = $result['id'];
		self::$userData[0] = $result;
		return true;
	}
	
	// function for logging out a user
	public static function logout(){
		if(isset($_SESSION['uid'])){
			$_SESSION['uid'] = 0;
		}
	}
	
	// function for adding user
	public static function add($data) {
		$db = DB::get();
		return $db->insert('user',$data);
	}
	
	// function for getting user data
	private static $userData = array();
	public static function getUserData($id = 0) {
		// settin up guest user if needed
		if(!isset($_SESSION['uid']) || $_SESSION['uid'] === 0){
			$_SESSION['uid'] = 0;
			self::$userData[0] = array(
				'username'    => 'Guest',
				'password'    => 'public',
				'permissions' => 'guest',
				'id_person'  => 0
			);
		}
		// gettin user id if not specified
		if($id === 0){
			$id = $_SESSION['uid'];
		}
		if(!isset(self::$userData[$id])){
			/*
			$db = DB::get();
			$sql = "
				select * from
				user u left join
				person p on u.id_person = p.id 
				where u.id = {$id}
			";
			self::$userData[$id] = $db->fetch_row($sql);
			*/
			$data = array(
				'Command'  => 'getTupleByID',
				'TableName' => 'user',
				'ID' => "{$id}"
			);
			$user = Socket::request($data);
			$person = DB_Person::getPersonData($user['id_person']);
			unset($person['id']);
			self::$userData[$id] = array_merge($user,$person);
		}
		return self::$userData[$id];
	}
	
	// function for checking if user exists
	public static function userExists($username){
		/*
		$db = DB::get();
		$sql = "
			select * from
			user 
			where
			username = '{$username}'
		";
		$check = $db->fetch_row($sql);
		*/
		$data = array(
			'Command'  => 'getTableOrderLimitSpecify',
			'TableName' => 'user',
			'SpecColumn' => 'username',
			'SpecValue' => "{$username}"
		);
		$result = Socket::request($data);
		if(count($result) > 0){
			return true;
		}
		return false;
	}
}
?>
