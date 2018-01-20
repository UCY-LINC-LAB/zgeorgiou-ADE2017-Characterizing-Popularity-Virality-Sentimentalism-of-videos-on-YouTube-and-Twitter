#!/bin/bash
VM="ubuntu@35.184.184.180"



if [[ "$#" != 1 ]] ; then
    echo "One argument is required"
    exit 1
fi

scp $1 "$VM:/home/ubuntu/$1"

