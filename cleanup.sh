#!/bin/bash

#
# Root directory of your project
#PROJDIR=$HOME/AOS/Project1
#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
#CONFIG=$PROJDIR/config.txt
#
# Directory your java classes are in
#
#BINDIR=$PROJDIR/bin
#
# Your main project class
#
PROG=Project2
NETID=$2
PROGRAM_PATH=$(pwd)

cat $1 | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    #netId=$( echo $i | awk '{ print $2 }' )
    totalNodes=$( echo $i | awk '{ print $1 }' )
    #echo $netId
    for ((a=1; a <= $totalNodes ; a++))
    do
    	read line 
	echo $line
       	host=$( echo $line | awk '{ print $2 }' )
	echo $host
	echo $NETID
        ssh $NETID@$host killall -u $NETID &
	sleep 1
    done
   
)


echo "Cleanup complete"
