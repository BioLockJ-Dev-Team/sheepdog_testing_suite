#!/bin/bash

# Docker test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/test/testCollection_functions.sh
testCollectionName=docker

beforeTests 

# feature tests
DIR=${SHEP}/test/feature
runTestSet ${DIR}/defaultProps/docker_testList.txt
runTestSet ${DIR}/exeProps/docker_testList.txt

# module tests
DIR=${SHEP}/test/module
runTestSet ${DIR}/assembly/docker_testList.txt 
runTestSet ${DIR}/email/docker_eamil_testList.txt
runTestSet ${DIR}/GenMod/docker_testList.txt
runTestSet ${DIR}/kraken2/docker_testList.txt 
runTestSet ${DIR}/kraken2Parser/docker_Kraken2ParserTestList.txt
runTestSet ${DIR}/rdp/docker_RdpTestList.txt
runTestSet ${DIR}/rdpParser/docker_RdpParser_TestList.txt
runTestSet ${DIR}/validationUtil/docker_testList.txt

# full pipeline
runTestSet ${SHEP}/test/docker/docker_testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests 
