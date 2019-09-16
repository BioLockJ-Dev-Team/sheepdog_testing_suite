# Print summary information after running a test collection

# testCollectionName= #cluster or local or docker

beforeTests(){
	PATH=${SHEP}/MockMain/resources:$PATH
	RES=${SHEP}/test/results_${testCollectionName}_testSet_NOT_IN_GIT.txt
	date > $RES
	start=$(date '+%s')
}

afterTests(){
	end=$(date '+%s')
	time=$(( $(($end - $start)) / 60 ))
	echo ""                                                2>&1 | tee -a $RES
	echo "* * * * * * * * * * * * * * * * * * * *"         2>&1 | tee -a $RES
	echo "Total time to run ${testCollectionName} test collection:  $time minutes"  2>&1 | tee -a $RES

	# print the quick-reference version of the results
	echo "* * * * * * * * * * * * * * * * * * * *"
	echo "Summary from $RES:"
	echo ""
	grep "Test results\|Number of tests that" $RES
}