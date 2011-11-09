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
		$_SESSION['uid'] = $result['uid'];
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
				'pid_person'  => 0
			);
		}
		// gettin user id if not specified
		if($id === 0){
			$id = $_SESSION['uid'];
		}
		if(!isset(self::$userData[$id])){
			$db = DB::get();
			$sql = "
				select * from
				user u left join
				person p on u.pid_person = p.pid 
				where uid = {$id}
			";
			self::$userData[$id] = $db->fetch_row($sql);
		}
		return self::$userData[$id];
	}
	
	// function for checking if user exists
	public static function userExists($username){
		$db = DB::get();
		$sql = "
			select * from
			user 
			where
			username = '{$username}'
		";
		$check = $db->fetch_row($sql);
		if($check === false){
			return true;
		}
		return false;
	}
}
?>