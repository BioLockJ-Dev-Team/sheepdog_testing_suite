#! /bin/bash

# $1 - an arg

MOD=$(dirname $PWD)
PIPE=$(dirname $MOD)
OUT="$MOD/output"
OUTFILE="$OUT/$1.txt"
META="../../*ImportMetadata/output/*.txt"

echo "Current working directory: ${PWD//$PIPE/<PIPE_ROOT>}"

echo "Script args: $@"

echo $@ > $OUTFILE
