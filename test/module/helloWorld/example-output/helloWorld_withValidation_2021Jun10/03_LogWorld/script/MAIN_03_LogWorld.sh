#!/bin/bash

# BioLockJ v1.4.0: ${scriptDir}/MAIN_03_LogWorld.sh

export BLJ=/Users/ieclabau/git/BioLockJ

pipeDir="/Users/ieclabau/git/sheepdog_testing_suite/MockMain/pipelines/helloWorld_withValidation_2021Jun10"
modDir="${pipeDir}/03_LogWorld"
scriptDir="${modDir}/script"
tempDir="${modDir}/temp"
logDir="${modDir}/log"
outputDir="${modDir}/output"

touch "${scriptDir}/MAIN_03_LogWorld.sh_Started"

exec 1>${logDir}/MAIN.log
exec 2>&1
cd ${scriptDir}

function scriptFailed() {
    echo "Line #${2} failure status code [ ${3} ]:  ${1}" >> "${scriptDir}/MAIN_03_LogWorld.sh_Failures"
    exit ${3}
}

function executeLine() {
    ${1}
    statusCode=$?
    [ ${statusCode} -ne 0 ] && scriptFailed "${1}" ${2} ${statusCode}
}

executeLine "${scriptDir}/03.0_GenMod.sh" ${LINENO}

touch "${scriptDir}/MAIN_03_LogWorld.sh_Success"
