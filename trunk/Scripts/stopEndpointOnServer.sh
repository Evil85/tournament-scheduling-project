#! /bin/bash

projectserver=${USER}@140.160.138.28

echo -===Stopping Endpoint Server===-

# Kill the server if it was running
ssh ${projectserver} -C "(pkill -9 java)"

