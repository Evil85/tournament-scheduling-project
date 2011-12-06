<?php
class DB_Venue {
	// this function adds a venue
	public static function add($data){
		$db = DB::get();
		return $db->insert('venue',$data);
	}
	
	// funciton gets a division list for a tournament
	private static $venueList = false;
	public static function getVenueList($id){
		if(self::$venueList === false) {
			$data = array(
				'Command'  => 'getLocationsByTournamentID',
				'TournamentID' => "{$id}"
			);
			self::$venueList = Socket::request($data);
			Debug::add('socket',self::$venueList);
		}
		return self::$venueList;
	}
}
?>