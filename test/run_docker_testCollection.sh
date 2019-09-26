#!/bin/bash

# Docker test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/test/testCollection_functions.sh
testCollectionName=docker

beforeTests 

# module tests
DIR=${SHEP}/test/module
runTestSet ${DIR}/assembly/docker_testList.txt 
runTestSet ${DIR}/validationUtil/docker_testList.txt
#runTestSet ${DIR}/kraken2/docker_testList.txt 

# full pipeline
runTestSet ${SHEP}/test/docker/docker_testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests 