#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp

cd relisApp

git checkout develop
pwd
cd relis_app
sudo mkdir -p logs
cd ..
ls
 
#copy the workspace 
sudo cp -r ../workspace workspace
echo last_pwd
pwd

cd .. && sudo chmod a+rwx relisApp -R && cd relisApp && ls -l
#echo changing permission of 'workspace' to 777
sudo chmod 777 -R workspace
echo chmod 777 to relisApp
cd relis_deployment
#build ReLis
sudo docker-compose build
# run ReLis app
sudo docker-compose up -d

sudo docker rm relis-application -f
sudo docker-compose up -d