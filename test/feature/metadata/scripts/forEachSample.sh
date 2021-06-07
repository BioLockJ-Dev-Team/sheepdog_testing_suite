#! /bin/bash

# $1 - a sample id

MOD=$(dirname $PWD)
PIPE=$(dirname $MOD)
OUT="$MOD/output"
OUTFILE="$OUT/$1.txt"
META="../../*ImportMetadata/output/metadata*"

echo "Current working directory: ${PWD//$PIPE/<PIPE_ROOT>}"

echo "Script args: $@"

while read line; do
  #echo "$line"
  ID=$(echo $line | awk '{print $1;}')
  echo "SAMPLE ID: $ID"
  VAL=$(echo $line | awk '{print $2;}')
  [ $ID == $1 ] && echo "Thats it!" && echo $VAL > $OUTFILE 
done < $META

echo "Done with $1."
#grep $1 $META > $OUTFILE
