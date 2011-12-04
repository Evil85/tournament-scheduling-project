#! /bin/bash

# Kill any previous servers that are running
sh stopEndpointOnLocal.sh

echo -===Starting Endpoint Server===-

# cd to the Project directory so that when we start the program all the log files will be created in the correct location
# Start the server
java -jar ../out/Endpoint.jar -p $1
