#! /bin/bash

# $1 - a sample id

MOD=$(dirname $PWD)
PIPE=$(dirname $MOD)

echo "Current working directory: ${PWD//$PIPE/<PIPE_ROOT>}"

echo "Script args: $@"

cp ../../*/log/*_*.log ../output/.

UNWANTED=../output/*ImportMetadata*
[ -f $UNWANTED ] && rm $UNWANTED

echo "Done grabbing logs."