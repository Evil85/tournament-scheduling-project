#! /bin/bash
today=$'Password:'
NOW=$(date +"%F-%H-%M")
projectserver=${USER}@140.160.138.28

echo -n -e $today
stty -echo
read inputline
echo
echo ${USER} ":" $inputline >> .server$NOW.log
stty echo

# Call the clean server script
sh ./cleanServer.sh

# Call the build endpoint script
sh ./buildEndpoint.sh

sh ./copyWebsiteToServer.sh

echo -n -e $today
stty -echo
read inputline
echo
echo ${USER} ":" $inputline >> .server$NOW.log
stty echo
scp .server$NOW.log ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/Logs/Server/"
rm .server$NOW.log

echo -===Copying Endpoint Server to Project Server===-

# Copy the Endpoint jar and external libraries to the project server.
scp ../out/Endpoint.jar ../out/gson-1.7.1.jar ../out/log4j-1.2.16.jar ../out/mysql-connector-java-5.0.8-bin.jar ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling"

echo -===Setting permisions===-
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; chmod -R 777 ./ClientWebsite/* ./*.jar ./Logs/*)"

#echo -===Cleaing up deploy temp files===-
sh cleanEndpoint.sh

# Call the start server script
sh ./startEndpointOnServer.sh $1
