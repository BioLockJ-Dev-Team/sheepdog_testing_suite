#! /bin/bash

# use:
# runMockMain.sh exampleTestList.txt

TESTER_JAR=${SHEP}/MockMain/dist/MockMain.jar
BLJ_JAR=${BLJ}/dist/BioLockJ.jar

if [ "$#" -eq 1 ]; then
	IN_FILE=$1
else
	echo "Oh dear.  runMockMain reqires one argument giving the path to the test list."
	exit 1
fi

# make a horizontal line in the console
echo "=========================================================="

java -cp ${TESTER_JAR}:${BLJ_JAR} sheepdog.RunMock ${IN_FILE}
