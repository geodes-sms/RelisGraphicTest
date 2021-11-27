#!/bin/bash


git clone https://github.com/geodes-sms/relis.git relisApp

cd relisApp
ls
git checkout develop
pwd

#copy the workspace 
cp -R ../workspace workspace
echo Copied @@@@@@@@@@@@@@@@@@
ls
echo le repertoire dsl_forge
cd workspace/dslforge_workspace
ls


#build ReLis
#sudo docker-compose build
# run ReLis app
#sudo docker-compose up -d
