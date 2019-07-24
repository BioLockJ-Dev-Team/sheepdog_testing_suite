
### Real Pipelines
. $BLJ/script/blj_user_lib
countPipelines(){
	echo $(ls $BLJ_PROJ | wc -l)
}
getPipeInfo(){ 
	blj_go > /dev/null
	pipeName=$(basename `pwd`)
	if [ -f biolockjComplete ] ; then
		STATUS="It completed."
	elif [ -f biolockjFailed ]; then
		STATUS="It failed."
	else
		STATUS="It\'s fate is unclear."
	fi
	cd - > /dev/null
	}
wrap_cmd_with_check(){
	CMD="$@"
	NUM_PIPES_PREV=$(countPipelines)
	($CMD) > /dev/null
	sleep 1
	NUM_PIPES_NOW=$(countPipelines)

	DIFF=$((NUM_PIPES_NOW - NUM_PIPES_PREV))
	if [ $DIFF -eq 1 ]; then
		getPipeInfo
		echo "Made pipeline ${pipeName}"
		echo "-->$STATUS"
	else
		echo "'OH NO! --> ' $CMD"
	fi
}

wrap_cmd_with_check biolockj $SHEP/test/module/commandLine/rarifySeqs.properties

wrap_cmd_with_check biolockj --docker $SHEP/test/module/commandLine/rarifySeqs.properties

wrap_cmd_with_check biolockj --external-modules $SHEP/MockMain/dist --docker $SHEP/test/module/commandLine/verifyRDPParser.properties

