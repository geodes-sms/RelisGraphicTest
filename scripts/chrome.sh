#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp
pwd
cd relisApp
git checkout develop
pwd


#build ReLis
sudo docker-compose build
# run ReLis app
sudo docker-compose up -d