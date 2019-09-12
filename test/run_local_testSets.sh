#!/bin/bash

# Local test collection

# Each 'run this test set' script prints output to the screen
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script a cliff-notes summary.

RES=${SHEP}/test/results_local_testSet_NOT_IN_GIT.txt
date > $RES

# module tests
DIR=${SHEP}/test/module
${DIR}/email/runThisTestSet.sh           2>&1 | tee -a $RES
${DIR}/rdp/runThisTestSet.sh             2>&1 | tee -a $RES
${DIR}/rdpParser/runThisTestSet.sh       2>&1 | tee -a $RES
${DIR}/validationUtil/runThisTestSet.sh  2>&1 | tee -a $RES
${DIR}/kraken2/runThisTestSet.sh         2>&1 | tee -a $RES
${DIR}/kraken2Parser/runThisTestSet.sh   2>&1 | tee -a $RES
${DIR}/GenMod/runThisTestSet.sh          2>&1 | tee -a $RES

# full pipeline
${SHEP}/test/local/runThisTestSet.sh     2>&1 | tee -a $RES

# print the quick-reference version of the results
echo "* * * * * * * * * * * * * * * * * * * *"
echo "Summary from $RES:"
echo ""
grep "Test results\|Number of tests that" $RES