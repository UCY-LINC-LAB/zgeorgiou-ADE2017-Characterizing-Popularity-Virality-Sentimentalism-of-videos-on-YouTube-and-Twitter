#!/bin/bash
VM="ubuntu@35.184.184.180"





echo "[Info] Moving Analysis artifact onto workstation"
scp /home/zgeorg03/Copy/ComputerScience/Thesis/yttresearch-service/yttresearch-analyzer/target/yttresearch-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar \
"$VM":/home/zgeorg03/Thesis-Project/analysis/
echo "[Info] Done"
