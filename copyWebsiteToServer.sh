#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Copying Client Website to Project Server===-
  
# Make a directory on the project server to put the php files in
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; mkdir ClientWebsite)"
	 
# Make a temp dir for the client website on the local machine so that we can remove all the .svn folders and then scp that folder to the project server.
	   
mkdir ./tempWebsite
cp -r ./ClientWebsite/ ./tempWebsite
find ./tempWebsite/ -name '.svn' -exec rm -rf {} \+
scp -r ./tempWebsite/* ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/ClientWebsite"
		    
echo -===Cleaning up website copy temp files===-
			  
rm -rf ./tempWebsite
