#!/bin/bash
NAME=backup_`date "+%Y-%m-%d"`
mongodump --db yttresearch1 -o $NAME
tar -czvf $NAME.tar.gz $NAME
rm -rf $NAME
