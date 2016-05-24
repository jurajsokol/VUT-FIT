#!/bin/ksh

HOSTNAME = `hostname`
PORT=80000
HOST="127.0.0.1"

# kompilacia
make
ls
echo "------------------------------------"
#vytorenie priecinkov a presun suborov
mkdir 's'
mv ./server ./s/server
mv ./xcx.png ./s/xcx.png

cd ./s/
./server -p "$PORT" & export SERVER_ID=$!
cd ../

./client -p "$PORT" -h "$HOSTNAME" -d xcx.png

./client -p "$PORT" -h "$HOSTNAME" -u Makefile

echo "server down"
kill $SERVER_ID
