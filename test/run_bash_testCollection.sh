#!/bin/bash

# Bash test collection

# Each 'run this test set' script prints output to the screen
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script a cliff-notes summary.

echo "part 1 ..."
${SHEP}/test/bash/wrap_testCommandLine.sh
echo "part 1 is done."

echo "part 2 ..."
${SHEP}/test/bash/wrap_basicRealTest.sh
echo "part 2 is done."

echo ""
echo "Done running bash tests."
