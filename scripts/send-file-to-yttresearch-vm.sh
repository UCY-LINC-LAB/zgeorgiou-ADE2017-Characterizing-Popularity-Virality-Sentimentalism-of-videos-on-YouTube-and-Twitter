#!/bin/bash
VM="ubuntu@10.16.3.12"

if [[ "$#" != 1 ]] ; then
    echo "One argument is required"
    exit 1
fi

scp $1 "$VM:/home/ubuntu/$1"

