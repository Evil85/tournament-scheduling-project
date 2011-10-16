#! /bin/bash

projectserver=${USER}@140.160.138.28
projectlocation='/ProjectSites/2011/Fall/Tournament\\ Scheduling/'

# Call the clean server script
sh ./cleanserver.sh

echo -===Building Endpoint Server===-

# Find all the java files in the Endpoint Program and store them in javasrc.txt
find ./EndpointProgram/src/ -name "*.java" > ./javasrc.txt

mkdir ./javaclasses

# Include the log4j configuration in the jar
cp ./EndpointProgram/src/log4j.xml ./javaclasses/

# Compile the java files into the javaclasses folder
javac -d ./javaclasses/ -cp ./EndpointProgram/libraries/gson-1.7.1.jar:./EndpointProgram/libraries/log4j-1.2.16.jar @javasrc.txt

# Create the Jar
jar -cmf ./EndpointProgram/src/META-INF/MANIFEST.MF Endpoint.jar -C ./javaclasses/ .

echo -===Copying Endpoint Server to Project Server===-

# Copy the Endpoint jar and external libraries to the project server.
scp ./Endpoint.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
scp ./EndpointProgram/libraries/gson-1.7.1.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"
scp ./EndpointProgram/libraries/log4j-1.2.16.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"

echo -===Copying Client Website to Project Server===-

# Make a directory on the project server to put the php files in
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; mkdir ClientWebsite)"

# Make a temp dir for the client website on the local machine so that we can remove all the .svn folders and then scp that folder to the project server.

mkdir ./tempWebsite
cp -r ./ClientWebsite/ ./tempWebsite/
find ./tempWebsite/ -name '.svn' -exec rm -rf {} \+
scp -r ./tempWebsite/* ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/ClientWebsite"

echo -===Cleaning up temp files===-

rm ./Endpoint.jar
rm ./javasrc.txt
rm -rf ./javaclasses

# Call the start server script
sh ./startserver.sh
