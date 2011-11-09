<?php
class User{
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
	
	public static function isMobile(){
		return (self::get_platform() == 'mobile');
	}
}
?>