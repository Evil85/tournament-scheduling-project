#! /bin/bash

projectserver=${USER}@140.160.138.28

# Call the clean server script
sh ./cleanServer.sh

# Call the build endpoint script
sh ./buildEndpoint.sh

sh ./copyWebsiteToServer.sh

echo -===Copying Endpoint Server to Project Server===-
  
# Copy the Endpoint jar and external libraries to the project server.
scp ./Endpoint.jar ./gson-1.7.1.jar ./log4j-1.2.16.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
#scp ./gson-1.7.1.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
#scp ./log4j-1.2.16.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
echo -===Setting permisions===-
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; chmod 775 ./ClientWebsite/* Endpoint.jar ./gson-1.7.1.jar ./log4j-1.2.16.jar ./Logs/*)"

echo -===Cleaing up deploy temp files===-

rm ./Endpoint.jar
rm ./gson-1.7.1.jar
rm ./log4j-1.2.16.jar

# Call the start server script
sh ./startEndpointOnServer.sh
