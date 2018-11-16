#!/bin/bash

#Checking to see if changes are made to a particular folder
if [[ $(sudo git diff --name-only origin/master $1) ]]
then
    echo "Changes to $1 folder detected. Will run $1 job"
else
    #exit status of non zero is request to trigger false condition in Conditional Build Step plugin
    exit 1
fi
#first change