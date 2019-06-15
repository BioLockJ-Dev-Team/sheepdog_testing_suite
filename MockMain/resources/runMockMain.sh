#! /bin/bash

TESTER_JAR=${SHEP}/MockMain/dist/MockMain.jar
BLJ_JAR=${BLJ}/dist/BioLockJ.jar
IN_FILE=${SHEP}/MockMain/resources/exampleTestList.txt
OUT_FILE=${SHEP}/MockMain/resources/exampleOutputFile.txt 

# make a horizontal line in the console
N=$(tput cols); T=$(seq 1 $N); for i in $T; do printf "="; done;

java -cp ${TESTER_JAR}:${BLJ_JAR} sheepdog.RunMock ${IN_FILE} ${OUT_FILE}
