<?php
class Permissions{
	private static $data = array(
		'guest' => array(
			'menu' => array(
				array(
					'label' => 'Home',
					'path'  => 'index.php',
					'icon'  => 'home_icon'
				),
				array(
					'label' => 'Login',
					'path'  => 'login.php',
					'icon'  => 'login_icon'
				)
			),
			'permissions' => array(
				'public'
			)
		),
		'admin' => array(
			'menu' => array(
				array(
					'label' => 'Home',
					'path'  => 'index.php',
					'icon'  => 'home_icon'
				),
				array(
					'label' => 'User List',
					'path'  => 'users.php',
					'icon'  => 'users_icon'
				),
				array(
					'label' => 'Profile',
					'path'  => 'profile.php',
					'icon'  => 'profile_icon'
				),
				array(
					'label' => 'Control',
					'path'  => 'admin.php',
					'icon'  => 'settings_icon'
				),
				array(
					'label' => 'Logout',
					'path'  => 'logout.php',
					'icon'  => 'logout_icon'
				)
			),
			'permissions' => array(
				'public',
				'admin'
			)
		)
	);
	public static function getMenu(){
		$user = User::get_user_info();
		return self::$data[$user['permissions']]['menu'];
	}
	public static function getPermissions(){
		return array_keys(self::$data);
	}
	public static function can_see($key){
		if (!is_array($key)) {
			$key = array($key);
		}
		$user = User::get_user_info();
		$intersection = array_intersect(
			self::$data[$user['permissions']]['permissions'],
			$key
		);
		Debug::add('p',$intersection);
		return count($intersection) > 0;
	}
}
?>