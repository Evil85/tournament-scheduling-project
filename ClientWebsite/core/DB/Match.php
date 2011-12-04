<?php
class DB_Match {
	// this function adds a match
	public static function add($data){
		$db = DB::get();
		return $db->insert('match',$data);
	}
	
	public static function getMatches($id){
		$data = array(
			'Command'  => 'getTableOrderLimitSpecify',
			'TableName' => 'match',
			'SpecColumn' => 'id_division',
			'SpecValue' => "{$id}"
		);
		$temp = Socket::request($data);
		Debug::add('socket',$temp);
		return $temp;
	}
}
?>
