#!/bin/sh

set -eu

echo "Checking DB connection ..."

i=0
until [ $i -ge 15 ]
do
  nc -z database 3306 && break

  i=$(( i + 1 ))

  echo "$i: Waiting for DB 5 second ..."

  sleep 5
done

if [ $i -eq 10 ]
then
  echo "DB connection refused, terminating ..."
  exit 1
fi

echo "DB is up ..."

java -jar /app/app.jar