#!/bin/bash

DEST=$1
OUT=../output

for f in $(ls $DEST)
do
	ln -s ${DEST}/$f $OUT/$f
done
