#! /bin/bash

SCRIPT=${SHEP}/test/bash/basicRealTest.sh

cd $BLJ
BLJ_VERSION=$(git describe --tags --always)
cd - > /dev/null

cd ${SHEP}
SHEP_VERSION=$(git describe --tags --always)
cd - > /dev/null

OUTPUT="${SHEP}/test/bash/basicRealTest_${BLJ_VERSION}_NOT_IN_GIT.txt"
echo "Saving results summary as: $OUTPUT"

echo "Most recent sheepdog commit: $SHEP_VERSION"  > $OUTPUT
echo "Most recent BioLockJ commit: $BLJ_VERSION"  >> $OUTPUT

$SCRIPT >> $OUTPUT

echo ">=================> Results summary: "
echo ""
cat $OUTPUT
echo ""
echo "<=================<"
