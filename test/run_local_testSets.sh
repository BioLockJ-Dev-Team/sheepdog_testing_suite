#!/bin/bash

# Local test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/MockMain/resources/testCollectionInfo.sh
testCollectionName=local

beforeTests # see testCollectionInfo.sh

# module tests
DIR=${SHEP}/test/module
#${DIR}/assembly/runThisTestSet.sh                2>&1 | tee -a $RES
testBiolockj ${DIR}/email/testList.txt           2>&1 | tee -a $RES
testBiolockj ${DIR}/rdp/RdpTestList.txt          2>&1 | tee -a $RES
testBiolockj ${DIR}/rdpParser/RdpParser_TestList.txt  2>&1 | tee -a $RES
testBiolockj ${DIR}/validationUtil/testList.txt  2>&1 | tee -a $RES
#testBiolockj ${DIR}/kraken2/testList.txt         2>&1 | tee -a $RES
#testBiolockj ${DIR}/kraken2Parser/testList.txt   2>&1 | tee -a $RES
testBiolockj ${DIR}/GenMod/testList.txt          2>&1 | tee -a $RES

# the sheepdog quickstart example
DIR=${SHEP}/MockMain/resources
${DIR}/runThisTestSet.sh                         2>&1 | tee -a $RES

# full pipeline
testBiolockj ${SHEP}/test/local/testList.txt     2>&1 | tee -a $RES

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh