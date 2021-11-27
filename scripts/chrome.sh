#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp
ls
cd relisApp
git checkout develop
pwd
sudo rm -r workspace

#copy the workspace 
cp -R ../workspace .
echo Copied @@@@@@@@@@@@@@@@@@
ls

#build ReLis
#sudo docker-compose build
# run ReLis app
#sudo docker-compose up -d
