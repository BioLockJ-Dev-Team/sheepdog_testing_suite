#! /bin/bash

# This script does not require any arguments
#
# For repeated testing during active development, 
# use the wrap_testCommandLine.sh.
#
# This test assumes that BioLockJ has been installed
# and that (as a result) the biolockj script directory
# is on the $PATH, as we expect it to be for users.

check_it(){
	echo "-"
	TOTAL_TESTS=$((TOTAL_TESTS + 1))
	#
	# for some tests, we expect messages in the .err files
	if [ -f $OUT/${id}.err ]; then
		errSize=$(du $OUT/${id}.err | cut -f 1)
		if [ $errSize -gt 0 ]; then
			echo "" >> $OUT/${id}.out
			echo "STANDARD_ERR:" >> $OUT/${id}.out
			cat $OUT/${id}.err >> $OUT/${id}.out
		fi
		rm $OUT/${id}.err
	fi
	#
	# compare the .out files to previous run.
	OUT_FILE=$(${SHEP}/test/bash/generalize.sh $OUT/${id}.out)
	EXP_FILE=$EXP/${id}_generic.out

	[ ! -f $EXP_FILE ] && "The expectation file for ${id}, [ $EXP_FILE ] does not exist."
	#
	# if $OUT_FILE and $EXP_FILE are identical, then hasDiff will be empty
	hasDiff=$(diff $OUT_FILE $EXP_FILE || echo "comparison failed")
	#
	# make conclusions from the comparison
	if [ ${#hasDiff} -gt 0 ]; then 
		echo "oh no! examine $id !"
		git --no-pager diff --no-index $EXP_FILE $OUT_FILE
	else # hasDiff is empty
		echo "$id --> just as expected."
		PASSING_TESTS=$((PASSING_TESTS + 1))
	fi
}

TOTAL_TESTS=0
PASSING_TESTS=0

OUT="$SHEP/test/feature/buildDocs/output"
EXP="$SHEP/test/feature/buildDocs/expected"
rm -rf $OUT
mkdir $OUT
echo "Output from individual tests are stored in: $OUT"

MORE_MODS=$SHEP/MockMain/dist
GENERATED_TEST=$BLJ/mkdocs/user-guide/docs/GENERATED

# standard use case
id=00_standard
java -cp $BLJ/dist/BioLockJ.jar biolockj.api.BuildDocs $GENERATED_TEST 1>> $OUT/${id}.out 2>> $OUT/${id}.err
check_it

id=01_independent
java -cp $BLJ/dist/BioLockJ.jar:${MORE_MODS}/* \
    biolockj.api.BuildDocs $OUT sheepdog.modules 1>> $OUT/${id}.out 2>> $OUT/${id}.err
cat $OUT/all-modules.md >> $OUT/${id}.out
echo "" >> $OUT/${id}.out
echo "Example Module Documentation:" >> $OUT/${id}.out
cat $OUT/sheepdog.modules/CopyMasterProps.md >> $OUT/${id}.out
check_it








echo ""
echo "Ran $TOTAL_TESTS tests on buildDocs utility."
if [ $TOTAL_TESTS -gt $PASSING_TESTS ]; then
	numFailed=$((TOTAL_TESTS - PASSING_TESTS))
	echo "There were $numFailed tests that FAILED !!!"
	exit 1
else
	echo "All $PASSING_TESTS tests PASS."
	exit 0
fi
