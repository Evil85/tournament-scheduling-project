#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Assembling Database===-
scp TournDB_Create_Script.sql ${projectserver}:"/ProjectSites/2011/Fall/Tournament\\ Scheduling/"
ssh ${projectserver} -C "(cd /ProjectSites/2011/Fall/Tournament\\ Scheduling/; chmod 777 TournDB_Create_Script.sql; mysql --user=admtourn201140 --password=yinvamOph -D tourn_201140 < TournDB_Create_Script.sql)"
