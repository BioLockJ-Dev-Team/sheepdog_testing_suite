#!/bin/bash

# Local test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/test/testCollection_functions.sh
testCollectionName=local

beforeTests # see testCollectionInfo.sh

# module tests
DIR=${SHEP}/test/module
#runTestSet ${DIR}/assembly/testList.txt   
runTestSet ${DIR}/email/testList.txt     
runTestSet ${DIR}/rdp/RdpTestList.txt 
runTestSet ${DIR}/rdpParser/RdpParser_TestList.txt
runTestSet ${DIR}/validationUtil/testList.txt 
#runTestSet ${DIR}/kraken2/testList.txt   
runTestSet ${DIR}/kraken2Parser/testList.txt 
runTestSet ${DIR}/GenMod/testList.txt 

# the sheepdog quickstart example
DIR=${SHEP}/MockMain/resources
runTestSet ${DIR}/testList.txt         

# full pipeline
runTestSet ${SHEP}/test/local/testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh