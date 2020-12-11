#! /bin/bash

# $1 - a sample id

MOD=$(dirname $PWD)
PIPE=$(dirname $MOD)
INFILE=$1
FNAME=$(basename $INFILE)
FNAME=${FNAME//".txt"/""}
OUT="$MOD/output"
OUTFILE="$OUT/${FNAME}_withHeader.txt"
META="../../*ImportMetadata/output/*.txt"

echo "Current working directory: ${PWD//$PIPE/<PIPE_ROOT>}"

echo "Script args: ${@//$PIPE/<PIPE_ROOT>}"

head -n 1 $META > $OUTFILE

while read line; do
  #echo "$line"
  echo -e ${line//" "/'\t'} >> $OUTFILE 
done < $INFILE

echo "Done with $FNAME."
