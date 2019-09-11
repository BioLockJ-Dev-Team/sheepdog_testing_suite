#! /bin/bash

SCRIPT=${SHEP}/test/bash/testCommandLine.sh

cd $BLJ
BLJ_VERSION=$(git describe --tags --always)
cd - > /dev/null

cd ${SHEP}
SHEP_VERSION=$(git describe --tags --always)
cd - > /dev/null

OUTPUT="${SHEP}/test/bash/testCommandLine_${BLJ_VERSION}_NOT_IN_GIT.txt"
echo "Saving results summary as: $OUTPUT"

echo "Most recent sheepdog commit: $SHEP_VERSION"  > $OUTPUT
echo "Most recent BioLockJ commit: $BLJ_VERSION"  >> $OUTPUT

cd $(dirname $SCRIPT)
$SCRIPT >> $OUTPUT
cd - > /dev/null

echo ">=================> Results summary: "
echo ""
cat $OUTPUT
echo ""
echo "<=================<"
