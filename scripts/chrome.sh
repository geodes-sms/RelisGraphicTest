#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp

cd relisApp

git checkout develop
pwd
cd relis_app
mkdir -p logs
cd ..
ls
 
#copy the workspace 
cp -R ../workspace workspace
echo last_pwd
pwd
echo changing permission of 'workspace' to 777
sudo chmod 777 -R workspace
echo chmod 777 to relisApp
cd .. && sudo chmod a+rwx relisApp -R && cd relisApp && ls -l
cd relis_deployment
#build ReLis
sudo docker-compose build
# run ReLis app
sudo docker-compose up -d
