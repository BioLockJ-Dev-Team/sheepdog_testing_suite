#!/bin/bash

DEST=${SHEP}/test/module/sraDownload/downloads
OUT=$(echo ${SHEP}/MockMain/pipelines/externalDestDir_*/0*GenMod/output)

for f in $(ls $DEST)
do
	ln -s ${DEST}/$f $OUT/$f
done
