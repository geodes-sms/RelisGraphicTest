#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp

cd relisApp
ls
git checkout develop
pwd

#copy the workspace 
cp -R ../workspace workspace
echo changing permission of 'workspcae' to 777
sudo chmod 777 -R workspace

cd relis_deployment
#build ReLis
sudo docker-compose build
# run ReLis app
sudo docker-compose up -d
