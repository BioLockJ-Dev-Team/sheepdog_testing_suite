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
runTestSet ${DIR}/asSyntax/docker_asSyntax_testList.txt
#runTestSet ${DIR}/defaultProps/docker_defaultProps_testList.txt
runTestSet ${DIR}/exeProps/docker_exeProps_testList.txt
runTestSet ${DIR}/metadata/docker_metadata_testList.txt
runTestSet ${DIR}/precheck/docker_precheck_testList.txt
#runTestSet ${DIR}/summary/summary_testList.txt #currently does not have functional docker tests
runTestSet ${DIR}/tutorialSeries/docker_tutorials_testList.txt

# module tests
DIR=${SHEP}/test/module
runTestSet ${DIR}/assembly/docker_assembly_testList.txt 
#runTestSet ${DIR}/calcStats/docker_calcStats_testList.txt
runTestSet ${DIR}/email/docker_eamil_testList.txt
runTestSet ${DIR}/GenMod/docker_genMod_testList.txt
runTestSet ${DIR}/kraken2/docker_k2_testList.txt 
runTestSet ${DIR}/kraken2Parser/docker_Kraken2ParserTestList.txt
runTestSet ${DIR}/normalizeTaxa/docker_normTaxa_testList.txt
runTestSet ${DIR}/rdp/docker_RdpTestList.txt
runTestSet ${DIR}/rdpParser/docker_RdpParser_TestList.txt
runTestSet $DIR/sraMetaData/docker_sraMetaData_testList.txt
runTestSet ${DIR}/validationUtil/docker_validation_testList.txt

# full pipeline
runTestSet ${SHEP}/test/docker/docker_testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests 
