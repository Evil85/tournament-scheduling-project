<?php
	$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
	socket_connect($socket, "localhost", "8000");
	socket_write($socket, "{ Command : createUser, Username : Bob }\n\r");
	$result = "";
	while ($read = socket_read($socket, 2024))
	{  
		if ($read == '\r')
			break;
		$result .= $read;
		echo $result;
	}
										 
	echo "Result recieved: '$result'\n";
	socket_close($socket);
?>
