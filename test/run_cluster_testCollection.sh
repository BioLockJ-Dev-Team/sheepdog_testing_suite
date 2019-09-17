
#!/bin/bash

# Local test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

. ${SHEP}/test/testCollection_functions.sh
testCollectionName=cluster

beforeTests # see testCollectionInfo.sh

DIR=${SHEP}/test/module/assembly
#runTestSet $DIR/cluster_testList.txt

# In theory, any local pipeline can run on the cluster as long 
# as the file paths are provided correctly in local.properties.

# local module tests
DIR=${SHEP}/test/module
#runTestSet ${DIR}/assembly/testList.txt 
#runTestSet ${DIR}/email/testList.txt 
runTestSet ${DIR}/rdp/RdpTestList.txt 
runTestSet ${DIR}/rdpParser/RdpParser_TestList.txt 
runTestSet ${DIR}/validationUtil/testList.txt
runTestSet ${DIR}/kraken2/testList.txt 
runTestSet ${DIR}/kraken2Parser/Kraken2ParserTestList.txt
runTestSet ${DIR}/GenMod/testList.txt 

# full local pipeline
runTestSet ${SHEP}/test/local/testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh