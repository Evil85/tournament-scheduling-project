<?php
// starting session
session_start();

class Socket{
	private static $socket = false;
	public static function get(){
		//if(self::$socket === false){
			$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
			socket_connect($socket, 'localhost', 2345);
			//self::$socket = $socket;
		//}
		//return self::$socket;
		return $socket;
	}
	public static function request($data){
		$socket = self::get();
		$message = json_encode($data);
		socket_write($socket, $message);
    	socket_write($socket, "\r");
		while($read = socket_read($socket, 1024)) {
			$result .= $read;
		}
		socket_close($socket);
		return self::rjasond(json_decode($result,true));
	}
	public static function rjasond($data){
		foreach($data as $key => $row){
			if(is_numeric($key)){
				$data[$key] = json_decode($row,true);
			}
		}
		return $data;
	}
}


function getSocketMessage($data){
	$message = json_encode($data);
	$message .= '\r';
	return $message;
}

// autoloader
function __autoload($class){
	$path = 'core/'.implode('/',explode('_',$class)).'.php';
	if(file_exists($path)){
		require_once($path);
	}
}
Init::user();
?>
