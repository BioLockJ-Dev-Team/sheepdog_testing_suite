#! /bin/bash

INPUT_NAME=$1
OUTPUT_NAME=${INPUT_NAME%.out}_generic.out

DATE=$(date '+%Y%b%d')
VERSION=$(cat ${SHEP}/biolockjVersion)

while read -r line; do
	# replace SHEP
	line=${line//"${SHEP}"/"<SHEP>"}
	# replace BLJ_PROJ
	line=${line//"${BLJ_PROJ}"/"<BLJ_PROJ>"}
	# replace BLJ
	line=${line//"$BLJ"/"<BLJ>"}
	# replace todays date
	line=${line//"${DATE}"/"<DATE>"}
	# replace current version
	line=${line//"${VERSION}"/"<VERSION>"}
	# write to output
	echo $line >> $OUTPUT_NAME	
done < $1

echo $OUTPUT_NAME