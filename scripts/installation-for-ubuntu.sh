#!/bin/bash


sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5

echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.6 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.6.list

sudo apt-get update


echo "Installing Java Runtime..."
sudo apt-get install -y default-jre

echo "Installing mongodb..."
sudo apt-get install -y mongodb-org

sudo systemctl enable mongod
sudo systemctl start mongod



