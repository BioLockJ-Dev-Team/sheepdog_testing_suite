#! /bin/bash

SCRIPT=${SHEP}/test/module/commandLine/basicRealTest.sh

cd $BLJ
BLJ_VERSION=$(git describe --tags --always)
cd -

cd ${SHEP}/test/module/commandLine
SHEP_VERSION=$(git describe --tags --always)
cd -

OUTPUT="NOT_IN_GIT_basicRealTest_${BLJ_VERSION}_$(date +%Y-%m-%d_%H-%M).txt"

echo "Most recent sheepdog commit: $SHEP_VERSION" > $OUTPUT
$SCRIPT >> $OUTPUT

echo ">=================>"
cat $OUTPUT
echo "<=================<"
