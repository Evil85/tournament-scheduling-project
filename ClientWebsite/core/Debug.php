<?php
class Debug{
	private static $data = array();
	
	public static function add($key,$data){
		if(!isset(Debug::$data[$key])){
			Debug::$data[$key] = '';
		}
		if(is_array($data)){
			Debug::$data[$key] .= '<pre>'.print_r($data,true).'</pre>';
		} else{
			Debug::$data[$key] .= $data.'<br/>';
		}
	}
	
	public static function generate(){
		if(isset($_GET['d'])){
			$params = explode(',',$_GET['d']);
			foreach($params as $param){
				if(array_key_exists($param,self::$data)){
					echo '<h2>'.$param.'</h2>';
					echo self::$data[$param];
				} else {
					echo '<h2>No Debug Data Available for: '.$param.'</h2>';
				}
			}
		}
	}
}
?>