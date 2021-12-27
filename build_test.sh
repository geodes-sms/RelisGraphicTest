#!/usr/bin/env bash

chmod +x scripts/build.sh

## Run the job that needs to be run as root
sh scripts/build.sh

mvn clean test


