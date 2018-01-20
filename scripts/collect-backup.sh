#!/bin/bash
VM="ubuntu@35.184.184.180"
scp "$VM:/home/ubuntu/backup_2018-$1.tar.gz" .
