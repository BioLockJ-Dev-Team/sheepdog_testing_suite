#!/bin/bash

# BioLockJ.v1.4.0: ${scriptDir}/04.0_Hello_Friends.sh

export BLJ=/Users/ieclabau/git/BioLockJ

pipeDir="/Users/ieclabau/git/sheepdog_testing_suite/MockMain/pipelines/helloWorld_withValidation_2021Jun10"
modDir="${pipeDir}/04_Hello_Friends"
scriptDir="${modDir}/script"
tempDir="${modDir}/temp"
logDir="${modDir}/log"
outputDir="${modDir}/output"

touch "${scriptDir}/04.0_Hello_Friends.sh_Started"

exec 1>${logDir}/04.0_Hello_Friends.log
exec 2>&1

cd ${scriptDir}

function sayHello(){
    echo 'The message is: Hello, Jean-Luc!!!'
    echo 'Hello, Jean-Luc!!!' > ../output/hello.txt
}
function scriptFailed() {
    echo "Line #${2} failure status code [ ${3} ]:  ${1}" >> "${scriptDir}/04.0_Hello_Friends.sh_Failures"
    exit ${3}
}

function executeLine() {
    ${1}
    statusCode=$?
    [ ${statusCode} -ne 0 ] && scriptFailed "${1}" ${2} ${statusCode}
}

executeLine "sayHello" ${LINENO}
touch "${scriptDir}/04.0_Hello_Friends.sh_Success"
echo 'Created Success flag.'
