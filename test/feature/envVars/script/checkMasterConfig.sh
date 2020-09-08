#! /bin/bash

MASTER=$(ls ../../MASTER*.properties)
OUT=../output/recordedEnvVars.txt

grep "TEST_VAR=" $MASTER >> $OUT
grep "pipeline.envVars" $MASTER >> $OUT
