#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Assembling Database===-

ssh ${projectserver} -C "(mysql --user=admtourn201140 --password=yinvamOph -D tourn_201140 < TournDB_Create_Script.sql)"
