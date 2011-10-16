<?php
	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	socket_connect($socket, "localhost", "8000");
	socket_write($socket, "{ Command : createUser, Username : Bob }\n\r");
	$result = "";
	while ($read = socket_read($socket, 1024))
	{   
		$result .= $read;
	}
										 
	echo "Result recieved: '$result'\n";
	socket_close($socket);
?>
