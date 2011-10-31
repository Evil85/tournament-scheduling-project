<?php
class User{
	// user info
	private static $user_info = array();
	
	// user list
	private static $user_list = false;
	
	// this function loads the user for the current session
	private static function load_user($uid){
		if($uid == 0){
			return array(
				'uid'         => 0,
				'username'    => 'Guest',
				'password'    => 'public',
				'permissions' => 'guest',
				'pid_person'  => 0
			);
		}
		$db = DB::get();
		$sql = "
			select * from
			user u 
			left join
			person p on u.pid_person = p.pid
			where
			u.uid = {$uid}
			limit 1
		";
		$result = $db->fetch_row($sql);
		if($result === false){
			return false;
		}
		return $result;
	}
	
	// this function logs in the user
	public static function login($username,$password){
		// needs validation code
		$db = DB::get();
		$sql = "
			select uid from
			user
			where
			username = '$username' and
			password = '$password'
			limit 1
		";
		$result = $db->fetch_row($sql);
		if($result === false){
			return false;
		}
		$_SESSION['uid'] = $result['uid'];
		return true;
	}
	
	public static function logout(){
		$_SESSION['uid'] = 0;
		self::load_user(0);
	}
	
	// user info accessor
	public static function get_user_info($uid = 0, $refresh = false){
		// 0 means get logged in users data
		if($uid == 0){
			if(isset($_SESSION['uid'])){
				$uid = $_SESSION['uid'];
			} else {
				$_SESSION['uid'] = 0;
			}
		}
		if(!isset(self::$user_info[$uid]) || $refresh){
			self::$user_info[$uid] = self::load_user($uid);
		}
		
		return self::$user_info[$uid];
	}
	
	public static function is_public(){
		if(isset($_SESSION['uid']) && $_SESSION['uid'] != 0){
			return false;
		}
		return true;
	}
	
	public static function is_site_admin(){
		$info = self::get_user_info();
		if(isset($info['permissions']) && $info['permissions'] == 'site_admin'){
			return true;
		}
		return false;
	}
	
	public static function get_platform(){
		if(isset($_GET['p']) && in_array($_GET['p'],array('computer','mobile'))){
			$_SESSION['platform'] = $_GET['p'];
		}
		if(isset($_SESSION['platform'])){
			return $_SESSION['platform'];
		}
		$browser = new Browser();
		if($browser->ismobile()){
			$_SESSION['platform'] = 'mobile';
		} else {
			$_SESSION['platform'] = 'computer';
		}
		return $_SESSION['platform'];
	}
	
	public static function add($data){
		$db = DB::get();
		// needs validation code
		return $db->insert('user',$data);
	}
	
	public static function get_user_list(){
		if(self::$user_list === false){
			$db = DB::get();
			$sql = "
				select * from 
				user u,
				person p
				where
				u.uid != 0 and
				u.pid_person = p.pid
			";
			self::$user_list = $db->fetch_all($sql);
		}
		return self::$user_list;
	}
	
	// for debug purposes
	public static function debug(){
		Debug::add('user',self::get_user_info());
	}
}
?>