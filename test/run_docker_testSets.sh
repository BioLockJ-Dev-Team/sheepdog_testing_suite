#!/bin/bash

# Docker test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/MockMain/resources/testCollectionInfo.sh
testCollectionName=docker

beforeTests # see testCollectionInfo.sh

# module tests
DIR=${SHEP}/test/module
testBiolockj ${DIR}/assembly/docker_testList.txt        2>&1 | tee -a $RES
#testBiolockj ${DIR}/kraken2/docker_testList.txt         2>&1 | tee -a $RES

# full pipeline
testBiolockj ${SHEP}/test/docker/docker_testList.txt    2>&1 | tee -a $RES

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh