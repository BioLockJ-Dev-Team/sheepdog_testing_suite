#!/bin/bash

# BioLockJ.v1.4.0: ${scriptDir}/00.0_Step5.sh

export BLJ=/Users/ieclabau/git/BioLockJ

pipeDir="/Users/ieclabau/git/sheepdog_testing_suite/MockMain/pipelines/helloWorld_withValidation_2021Jun10"
modDir="${pipeDir}/00_BasicSteps"
scriptDir="${modDir}/script"
tempDir="${modDir}/temp"
logDir="${modDir}/log"
outputDir="${modDir}/output"

touch "${scriptDir}/00.0_Step5.sh_Started"

exec 1>${logDir}/00.0_Step5.log
exec 2>&1

cd ${scriptDir}

function scriptFailed() {
    echo "Line #${2} failure status code [ ${3} ]:  ${1}" >> "${scriptDir}/00.0_Step5.sh_Failures"
    exit ${3}
}

function executeLine() {
    ${1}
    statusCode=$?
    [ ${statusCode} -ne 0 ] && scriptFailed "${1}" ${2} ${statusCode}
}

executeLine "echo Hello, world." ${LINENO}
touch "${scriptDir}/00.0_Step5.sh_Success"
echo 'Created Success flag.'
