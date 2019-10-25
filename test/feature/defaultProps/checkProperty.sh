#!/bin/bash

MASTER_PROP_FILE=$1
PROP_NAME=$2
PROP_VAL=$3

OUT="../output/results.txt"

CORRECT_LINE="${PROP_NAME}=${PROP_VAL}"
FOUND_LINE=$(grep "${PROP_NAME}=" $MASTER_PROP_FILE)

echo "Expected: $CORRECT_LINE" > $OUT
echo "Found: $FOUND_LINE" >> $OUT

if [ $FOUND_LINE == $CORRECT_LINE ]; then
	echo "Good!" >> $OUT
	exit 0
else
	echo "That is not correct." >> $OUT
	exit 1
fi