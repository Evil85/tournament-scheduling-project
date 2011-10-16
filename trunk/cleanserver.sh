#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Cleaning Endpoing Server from Project Server===-

#cd to the project server and remove the jars
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; nohup rm Endpoint.jar gson-1.7.1.jar log4j-1.2.16.jar)"

