#! /bin/bash

projectserver=${USER}@140.160.138.28

# Call the clean server script
sh ./cleanServer.sh

# Call the build endpoint script
sh ./buildEndpoint.sh

sh ./copyWebsiteToServer.sh

sh ./copyEndpointToServer.sh

echo -===Cleaing up deploy temp files===-

rm ./Endpoint.jar

# Call the start server script
sh ./startEndpoint.sh
