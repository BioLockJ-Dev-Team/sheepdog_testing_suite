
### Real Pipelines
. $BLJ/script/blj_functions
. $BLJ/script/blj_user_lib
countPipelines(){
	echo $(ls $BLJ_PROJ | wc -l)
}
getPipeInfo(){ 
	blj_go > /dev/null
	pipeName=`pwd`
	if [ -f biolockjStarted ]; then
		printf "It is running"
		while [ -f biolockjStarted ]; do
			printf "."
			sleep 1
		done
	fi
	if [ -f biolockjComplete ] ; then
		STATUS="It completed."
		PASSING_TESTS=$((PASSING_TESTS + 1))
	elif [ -f biolockjFailed ]; then
		STATUS="It failed."
	else
		STATUS="It's fate is unclear."
	fi
	cd - > /dev/null
	}
wrap_cmd_with_check(){
	TOTAL_TESTS=$((TOTAL_TESTS + 1))
	CMD="$@"
	echo " Test $TOTAL_TESTS ---> $CMD"
	NUM_PIPES_PREV=$(countPipelines)
	$CMD
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

DIR=$SHEP/test/bash/configFile

TOTAL_TESTS=0
PASSING_TESTS=0

wrap_cmd_with_check biolockj $DIR/rarifySeqs.properties

wrap_cmd_with_check biolockj --docker -e SHEP=$SHEP $DIR/rarifySeqs.properties

#wrap_cmd_with_check biolockj --external-modules $SHEP/MockMain/dist --docker $DIR/verifyRDPParser.properties

if [ $TOTAL_TESTS -gt $PASSING_TESTS ]; then
	numFailed=$((TOTAL_TESTS - PASSING_TESTS))
	echo "There were $numFailed tests that FAILED !!!"
	exit 1
else
	echo "Ran $TOTAL_TESTS basic launch process tests, all PASSED."
	exit 0
fi
