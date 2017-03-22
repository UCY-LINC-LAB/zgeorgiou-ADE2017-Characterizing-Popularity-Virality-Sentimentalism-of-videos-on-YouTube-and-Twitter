#!/bin/bash


echo "[Info] Moving Analysis scripts onto workstation"
scp -r /home/zgeorg03/Copy/ComputerScience/Thesis/yttresearch-service/yttresearch-analyzer/scripts/* \
zgeorg03@10.16.21.218:/home/zgeorg03/Thesis-Project/analysis/scripts/
echo "[Info] Done"
