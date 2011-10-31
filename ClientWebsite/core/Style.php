<?php
class Style{
	private static $list     = array();
	private static $packages = array();
	public static function add($style){
		self::$list[] = $style;
	}
	public static function get($package){
		// checking for mobile packages
		if(User::get_platform() == 'mobile'){
			$tmp = $package.'-mobile';
			if(file_exists('style/'.str_replace('_','/',$tmp).'.css')){
				$package = $tmp;
			}
		}
		// adding package
		if(!in_array($package,self::$packages)){
			self::$packages[] = $package;
			self::link('style/'.str_replace('_','/',$package).'.css');
		}
	}
	public static function link($url){
		self::$list[] = '<link rel="stylesheet" href="'.$url.'">';
	}
	public static function generate(){
		foreach(self::$list as $style){
			echo $style;
		}
	}
}
?>