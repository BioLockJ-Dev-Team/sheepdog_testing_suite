#!/bin/bash

# BioLockJ.v1.4.0: ${scriptDir}/02.0_Hello_World.sh

export BLJ=/Users/ieclabau/git/BioLockJ

pipeDir="/Users/ieclabau/git/sheepdog_testing_suite/MockMain/pipelines/helloWorld_withValidation_2021Jun10"
modDir="${pipeDir}/02_Hello_World"
scriptDir="${modDir}/script"
tempDir="${modDir}/temp"
logDir="${modDir}/log"
outputDir="${modDir}/output"

touch "${scriptDir}/02.0_Hello_World.sh_Started"

exec 1>${logDir}/02.0_Hello_World.log
exec 2>&1

cd ${scriptDir}

function sayHello(){
    echo 'The message is: Hello World!'
    echo 'Hello World!' > ../output/hello.txt
}
function scriptFailed() {
    echo "Line #${2} failure status code [ ${3} ]:  ${1}" >> "${scriptDir}/02.0_Hello_World.sh_Failures"
    exit ${3}
}

function executeLine() {
    ${1}
    statusCode=$?
    [ ${statusCode} -ne 0 ] && scriptFailed "${1}" ${2} ${statusCode}
}

executeLine "sayHello" ${LINENO}
touch "${scriptDir}/02.0_Hello_World.sh_Success"
echo 'Created Success flag.'
