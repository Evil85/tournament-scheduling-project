#! /bin/bash
echo User : ${USER}
echo Copying Client Website to Server.
scp -r ./ClientWebsite/ ${USER}@140.160.138.28:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/"

echo Copying Java Server to Server.
scp -r ./EndpointProgram/ ${USER}@140.160.138.28:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/"

echo Starting Endpiont Server.
ssh ${USER}@140.160.138.28 -C "(nohup java -jar /ProjectSites/2011/Fall/Tournament\ Scheduling/EndpointProgram/Endpoint)"
