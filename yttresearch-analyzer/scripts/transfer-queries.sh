#!/bin/bash

HOST=10.16.21.216

echo "[Info] Moving queries onto workstation"
scp -r /home/zgeorg03/Copy/ComputerScience/Thesis/yttresearch-service/yttresearch-analyzer/queries/* \
zgeorg03@$HOST:/home/zgeorg03/Thesis-Project/analysis/queries/
echo "[Info] Done"
