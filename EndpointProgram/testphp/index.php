<?php

	$address = "127.0.0.1";
	$port = 2345;

// getter testing: leave this here to verify connection is working

	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	socket_connect($socket, $address, $port);
    $data = array(
	    'Command'  => 'getTournamentID',
	    'TournamentName' => 'Clone Wars Racquetball'
 	);

 	$message = json_encode($data);
	socket_write($socket, $message);
    socket_write($socket, "\r");
	$result = "";
	while($read = socket_read($socket, 1024)) {
		$result .= $read;
	}

	echo "Result received, ARC's id: '$result'\n";
	$decodedResult = json_decode($result);
	$id = $decodedResult->{'result'};
	echo "decoded id: $id \n";
	socket_close($socket);




    // scheduleTournamentTesting testing
	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	socket_connect($socket, $address, $port);

    $data = array(
	    'Command'  => 'scheduleTournament',
	    'TournamentID' => "$id"
 	);
 	
 	$message = json_encode($data);
	socket_write($socket, $message);
    socket_write($socket, "\r");
	$result = "";
    
	while($read = socket_read($socket, 1024)) {
		$result .= $read;
	}
	echo "Result received, tournamentdata id=$id?: '$result'\n";

	socket_close($socket);

?>
