#!/bin/bash

# Docker test collection

# Each 'run this test set' script prints output to the screen.
# Each line pipes that output to tee so it is
# printed for you to see in real-time AND saved to a file.
# At the end, this script prints a cliff-notes summary.

RES=${SHEP}/test/results_docker_testSet_NOT_IN_GIT.txt
date > $RES
start=$(date '+%s')

# module tests
DIR=${SHEP}/test/module
${DIR}/assembly/docker_runThisTestSet.sh        2>&1 | tee -a $RES
#${DIR}/kraken2/docker_runThisTestSet.sh         2>&1 | tee -a $RES

# full pipeline
${SHEP}/test/docker/docker_runThisTestSet.sh    2>&1 | tee -a $RES

end=$(date '+%s')
time=$(( $(($end - $start)) / 60 ))
echo ""
echo "Total time to run tests:  $time minutes"  2>&1 | tee -a $RES

# print the quick-reference version of the results
echo "* * * * * * * * * * * * * * * * * * * *"
echo "Summary from $RES:"
echo ""
grep "Test results\|Number of tests that" $RES