<?php
class Script{
	private static $list     = array();
	private static $packages = array();
	public static function add($script){
		self::$list[] = '<script>'.$script.'</script>';
	}
	public static function get($package){
		if(!in_array($package,self::$packages)){
			self::$packages[] = $package;
			self::link('script/'.str_replace('_','/',$package).'.js');
		}
	}
	public static function link($url){
		self::$list[] = '<script src="'.$url.'"></script>';
	}
	public static function generate(){
		foreach(self::$list as $script){
			echo $script;
		}
	}
}
?>