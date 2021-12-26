#!/bin/bash

relis_app='relis_dev'
# switch to the root 
sudo su && echo 'not a linux os'
git clone https://github.com/geodes-sms/relis.git $relis_app

cd $relis_app

git checkout develop
pwd
cd relis_app
mkdir -p logs
cd ..
ls
 
#copy the workspace 
cp -r ../workspace workspace
echo last_pwd
pwd

cd .. && chmod a+rwx $relis_app -R && cd $relis_app && ls -l
#echo changing permission of 'workspace' to 777
chmod 777 -R workspace
cd relis_deployment
#build ReLis
docker-compose build
# run ReLis app
docker-compose up -d

docker rm relis-application -f
docker-compose up -d
