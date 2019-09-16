
#!/bin/bash

# Local test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/MockMain/resources/testCollectionInfo.sh
testCollectionName=cluster

beforeTests # see testCollectionInfo.sh

DIR=${SHEP}/test/module/assembly
#testBiolockj $DIR/cluster_testList.txt                 2>&1 | tee -a $RES

# In theory, any local pipeline can run on the cluster as long 
# as the file paths are provided correctly in local.properties.

# local module tests
DIR=${SHEP}/test/module
#testBiolockj ${DIR}/assembly/testList.txt              2>&1 | tee -a $RES
#testBiolockj ${DIR}/email/testList.txt                 2>&1 | tee -a $RES
testBiolockj ${DIR}/rdp/RdpTestList.txt                   2>&1 | tee -a $RES
#testBiolockj ${DIR}/rdpParser/testList.txt             2>&1 | tee -a $RES
testBiolockj ${DIR}/validationUtil/testList.txt        2>&1 | tee -a $RES
#testBiolockj ${DIR}/kraken2/testList.txt               2>&1 | tee -a $RES
#testBiolockj ${DIR}/kraken2Parser/testList.txt         2>&1 | tee -a $RES
testBiolockj ${DIR}/GenMod/testList.txt                2>&1 | tee -a $RES

# full local pipeline
testBiolockj ${SHEP}/test/local/testList.txt           2>&1 | tee -a $RES

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh
