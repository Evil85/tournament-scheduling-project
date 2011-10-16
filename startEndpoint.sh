#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Starting Endpoint Server===-

# Kill any previous servers that are running
# cd to the Project directory so that when we start the program all the log files will be created in the correct location
# Start the server
ssh ${projectserver} -C "(pkill -9 java; cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; java -jar Endpoint.jar)"
