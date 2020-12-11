#! /bin/bash

# $1 - a file

MOD=$(dirname $PWD)
PIPE=$(dirname $MOD)
OUT="$MOD/output"
BASE=$(basename $1)
FNAME="nChars-$BASE" 
OUTFILE="$OUT/$FNAME"

echo "Current working directory: ${PWD//$PIPE/<PIPE_ROOT>}"

echo "Script args: ${@//$PIPE/<PIPE_ROOT>}"

cat $1 | wc -c > $OUTFILE
