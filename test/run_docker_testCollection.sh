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
runTestSet ${DIR}/classLookup/docker_lookup_testList.txt
#runTestSet ${DIR}/defaultProps/docker_defaultProps_testList.txt
runTestSet ${DIR}/dotPath/docker_dot_testList.txt
runTestSet ${DIR}/envVars/docker_testList_envVars.txt
runTestSet ${DIR}/exeProps/docker_exeProps_testList.txt
runTestSet ${DIR}/fileNameInput/docker_testList_filename.txt
runTestSet ${DIR}/metadata/docker_metadata_testList.txt
runTestSet ${DIR}/precheck/docker_precheck_testList.txt
#runTestSet ${DIR}/summary/summary_testList.txt #currently does not have functional docker tests
runTestSet ${DIR}/tutorialSeries/docker_tutorials_testList.txt
runTestSet ${DIR}/verifyDocker/docker_verifyDocker_testList.txt

# module tests
DIR=${SHEP}/test/module
runTestSet ${DIR}/assembly/docker_assembly_testList.txt 
#runTestSet ${DIR}/calcStats/docker_calcStats_testList.txt
runTestSet ${DIR}/email/docker_eamil_testList.txt
runTestSet ${DIR}/genMod/docker_genMod_testList.txt
runTestSet ${DIR}/kraken2/docker_k2_testList.txt 
runTestSet ${DIR}/kraken2Parser/docker_Kraken2ParserTestList.txt
runTestSet ${DIR}/normalizeTaxa/docker_normTaxa_testList.txt
runTestSet ${DIR}/rdp/docker_RdpTestList.txt
runTestSet ${DIR}/rdpParser/docker_RdpParser_TestList.txt
runTestSet ${DIR}/rPlotMds/docker_testList_rPlotMds.txt
runTestSet ${DIR}/rPlotEffectSize/docker_testList_rPlotEffectSize.txt
runTestSet ${DIR}/rPlotOtus/docker_testList_rPlotOtus.txt
runTestSet ${DIR}/rPValHistograms/docker_testList_pValHist.txt
runTestSet ${DIR}/rmarkdown/docker_testList_rmd.txt
runTestSet ${DIR}/shannon/docker_shannonTestList.txt
runTestSet ${DIR}/sraDownload/docker_sraDownload_testList.txt
runTestSet ${DIR}/sraMetaData/docker_sraMetaData_testList.txt
runTestSet ${DIR}/validationUtil/docker_validation_testList.txt

# full pipeline
runTestSet ${SHEP}/test/docker/docker_testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests 
