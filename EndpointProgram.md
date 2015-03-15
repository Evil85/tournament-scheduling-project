This is a page that will describe what the endpoint is and how it works.

# Introduction #

The endpoint is a class java program that resides on the server. This program will be called from the PHP client. The calling client will pass in parameters based on what the client wants the endpoint to do. The parameters will be in the form of a Json string.


# Details #
The endpoint program is executed with the following line of code

`java -jar Endpoint.jar [JSON STRING]`

This can be called from PHP aswell

`exec("java -jar Endpoint.jar [JSON STRING]", $out);`

Where $out is the stdout from the Endpoint.jar program.