# bash wrapper functions

main(){
	SCRIPT=$1
	git_describe
	run_test
	print_output
	return_result
}

git_describe(){
	cd $BLJ
	BLJ_VERSION=$(git describe --tags --always)
	cd - > /dev/null

	cd ${SHEP}
	SHEP_VERSION=$(git describe --tags --always)
	cd - > /dev/null

	#OUTPUT="${SHEP}/test/bash/basicRealTest_${BLJ_VERSION}_NOT_IN_GIT.txt"
	OUTPUT="${SCRIPT%.sh}_${BLJ_VERSION}_NOT_IN_GIT.txt"

	echo "Saving results summary as: $OUTPUT"

	echo "Most recent sheepdog commit: $SHEP_VERSION"  > $OUTPUT
	echo "Most recent BioLockJ commit: $BLJ_VERSION"  >> $OUTPUT
}

run_test(){
	# The relative path outputs of the bash args 
	# require a consistent working directory.
	cd $(dirname $SCRIPT)
	$SCRIPT >> $OUTPUT && PASSING=true || PASSING=false
	cd - > /dev/null
}

print_output(){
	echo ">=================> Results summary: "
	echo ""
	cat $OUTPUT
	 ""
	echo "<=================<"
}

return_result(){
	if [ $PASSING ]; then
		exit 0
	else
		exit 1
	fi
}

main $@