<?php
class Error{
	private static $data = array();
	
	public static function add($key,$data){
		if(!isset(self::$data[$key])){
			self::$data[$key] = '';
		}
		if(is_array($data)){
			self::$data[$key] .= '<pre>'.print_r($data,true).'</pre>';
		} else{
			self::$data[$key] .= $data.'<br/>';
		}
	}
	
	public static function generate(){
		if(count(self::$data) > 0){
			foreach(self::$data as $param => $data){
				echo '<h2>'.$param.'</h2>';
				echo $data;
			}
		}
	}
}
?>