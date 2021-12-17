#!/bin/bash

relis_app='relis_dev'

git clone https://github.com/geodes-sms/relis.git $relis_app

cd $relis_app

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

cd .. && sudo chmod a+rwx $relis_app -R && cd $relis_app && ls -l
#echo changing permission of 'workspace' to 777
sudo chmod 777 -R workspace
cd relis_deployment
#build ReLis
sudo docker-compose build
# run ReLis app
sudo docker-compose up -d

sudo docker rm relis-application -f
sudo docker-compose up -d
