<?php
class Tournament{
	private static $list = false;
	private static $tournaments = array();
	private static $tounament_count = false;
	private static $divisions   = array();
	public static function add($data){
		$db = DB::get();
		return $db->insert('tournament',$data);
	}
	public static function addDivision($data){
		$db = DB::get();
		return $db->insert('division',$data);
	}
	public static function is_admin($id) {
		$t_info = self::getTournamentInfo($id);
		$u_info = User::get_user_info();
		return $t_info['uid_owner'] == $u_info['uid'];
	}
	public static function getTournaments($skip = 0,$get = 10){
		if(self::$list === false){
			$db = DB::get();
			$sql = "
				select * from 
				tournament
				where
				isGuestViewable = 1
				limit {$skip},{$get}
			";
			self::$list = $db->fetch_all($sql);
		} 
		return self::$list;
	}
	public static function getTournamentCount(){
		if(self::$tounament_count === false){
			$db = DB::get();
			$sql = "
				select count(*) as count from 
				tournament
				where
				isGuestViewable = 1
			";
			$count = $db->fetch_row($sql);
			self::$tounament_count = $count['count'];
		} 
		return self::$tounament_count;
	}
	public static function getTournamentInfo($id){
		if(!isset(self::$tournaments[$id])){
			$db = DB::get();
			$sql = "
				select * from
				tournament
				where
				tid = {$id}
			";
			self::$tournaments[$id] = $db->fetch_row($sql);
		}
		return self::$tournaments[$id];
	}
	public static function getDivisions($id){
		if(!isset(self::$divisions[$id])){
			$db = DB::get();
			$sql = "
				select * from
				division
				where
				tid_tournament = {$id}
			";
			self::$divisions[$id] = $db->fetch_all($sql);
		}
		return self::$divisions[$id];
	}
	public static function getTypes(){
		return array(
			'Round Robin' => 'round robin'
		);
	}
}
?>