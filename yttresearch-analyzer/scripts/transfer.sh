#!/bin/bash


echo "[Info] Moving Analysis artifact onto workstation"
scp /home/zgeorg03/Copy/ComputerScience/Thesis/yttresearch-service/yttresearch-analyzer/target/yttresearch-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar \
zgeorg03@10.16.21.216:/home/zgeorg03/Thesis-Project/analysis/
echo "[Info] Done"
