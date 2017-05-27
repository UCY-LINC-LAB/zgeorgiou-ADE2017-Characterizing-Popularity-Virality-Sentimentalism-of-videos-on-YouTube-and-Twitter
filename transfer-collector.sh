#!/bin/bash

echo "[Info] Moving Collector onto workstation"
scp ./yttresearch-collector/target/yttresearch-collector-1.0-SNAPSHOT-jar-with-dependencies.jar \
zgeorg03@10.16.21.218:/home/zgeorg03/Thesis-Project/analysis/
echo "[Info] Done"
