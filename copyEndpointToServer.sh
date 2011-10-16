#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Copying Endpoint Server to Project Server===-
  
# Copy the Endpoint jar and external libraries to the project server.
scp ./Endpoint.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
scp ./EndpointProgram/libraries/gson-1.7.1.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
scp ./EndpointProgram/libraries/log4j-1.2.16.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"

