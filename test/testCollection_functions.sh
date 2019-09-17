# Print summary information after running a test collection

# testCollectionName= #cluster or local or docker

beforeTests(){
	PATH=${SHEP}/MockMain/resources:$PATH
	LOG=${SHEP}/test/results_${testCollectionName}_testCollection_NOT_IN_GIT.txt
	rm $LOG
	date > $LOG
	RES=${SHEP}/test/results_${testCollectionName}_table_NOT_IN_GIT.txt
	rm $RES
	start=$(date '+%s')
}

# arg 1 - tests list
runTestSet(){
	testBiolockj $1 $RES    2>&1 | tee -a $LOG
}

afterTests(){
	end=$(date '+%s')
	time=$(( $(($end - $start)) / 60 ))
	echo ""                                                2>&1 | tee -a $LOG
	echo "* * * * * * * * * * * * * * * * * * * *"         2>&1 | tee -a $LOG
	echo "Total time to run ${testCollectionName} test collection:  $time minutes"  2>&1 | tee -a $LOG

	# print the quick-reference version of the results
	echo "* * * * * * * * * * * * * * * * * * * *"
	echo "Summary from $LOG:"
	echo ""
	grep "Test results\|Number of tests that" $LOG
}