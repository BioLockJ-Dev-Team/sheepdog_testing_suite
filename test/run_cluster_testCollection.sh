
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

# feature tests
DIR=${SHEP}/test/feature
runTestSet ${DIR}/asSyntax/asSyntax_testList.txt
runTestSet ${DIR}/classLookup/lookup_testList.txt
runTestSet ${DIR}/defaultProps/defaultProps_testList.txt
runTestSet ${DIR}/envVars/testList_envVars.txt
runTestSet ${DIR}/exeProps/exeProps_testList.txt
runTestSet ${DIR}/fileNameInput/testList_filename.txt
runTestSet ${DIR}/metadata/metadata_testList.txt
runTestSet ${DIR}/precheck/precheck_testList.txt
#runTestSet ${DIR}/summary/summary_testList.txt #currently slow and not very valuable
runTestSet ${DIR}/tutorialSeries/tutorials_testList.txt

# In theory, any local pipeline can run on the cluster as long 
# as the file paths are provided correctly in local.properties.

# local module tests
DIR=${SHEP}/test/module
#runTestSet ${DIR}/assembly/assembly_testList.txt 
runTestSet ${DIR}/calcStats/calcStats_testList.txt
#runTestSet ${DIR}/email/testList.txt 
runTestSet ${DIR}/genMod/genMod_testList.txt 
runTestSet ${DIR}/kraken2/k2_testList.txt 
runTestSet ${DIR}/kraken2Parser/Kraken2ParserTestList.txt
runTestSet ${DIR}/normalizeTaxa/normTaxa_testList.txt
runTestSet ${DIR}/rdp/RdpTestList.txt 
runTestSet ${DIR}/rdpParser/RdpParser_TestList.txt 
runTestSet ${DIR}/rPlotMds/testList_rPlotMds.txt
runTestSet ${DIR}/rPlotEffectSize/testList_rPlotEffectSize.txt
runTestSet ${DIR}/rPlotOtus/testList_rPlotOtus.txt
runTestSet ${DIR}/rPValHistograms/testList_pValHist.txt
runTestSet ${DIR}/rmarkdown/testList_rmd.txt
runTestSet ${DIR}/shannon/shannonTestList.txt
runTestSet ${DIR}/sraDownload/sraDownload_testList.txt
runTestSet ${DIR}/stop/testList_stop.txt
runTestSet ${DIR}/validationUtil/validation_testList.txt

# full local pipeline
runTestSet ${SHEP}/test/local/testList.txt 

# "* * * * * * * * * * * * * * * * * * * *"
afterTests # see testCollectionInfo.sh
