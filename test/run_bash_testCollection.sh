#!/bin/bash

# Bash test collection

TOTAL_TESTS=0
PASSING_TESTS=0
TESTS_THAT_FAILED=()

do_bash_test(){
	TOTAL_TESTS=$((TOTAL_TESTS + 1))
	echo "part $TOTAL_TESTS ..."
	# wrap_bash_tests.sh exits with 0 if test(s) pass, exits 1 otherwise
	${SHEP}/test/bash/wrap_bash_tests.sh $1 && PASSING_TESTS=$((PASSING_TESTS + 1)) || TESTS_THAT_FAILED=(${TESTS_THAT_FAILED[@]} $(basename $1) )
	echo "part $TOTAL_TESTS is done."
}

${SHEP}/MockMain/resources/testBioLockJ ${SHEP}/test/bash/empty_testList.txt 2>/dev/null || exit

do_bash_test ${SHEP}/test/bash/testAPI.sh

do_bash_test ${SHEP}/test/bash/testCommandLine.sh

do_bash_test ${SHEP}/test/bash/testRestartCylce.sh

do_bash_test ${SHEP}/test/bash/testRestartCylce_inDocker.sh

do_bash_test ${SHEP}/test/bash/basicRealTest.sh

# technically, this is a java feature, but this is the best place for it.
do_bash_test ${SHEP}/test/feature/buildDocs/testBuldDocs.sh

echo ""
echo "Done running bash tests."

if [ $TOTAL_TESTS -gt $PASSING_TESTS ]; then
	echo "The following test(s) FAILED!!!"
	for fail in ${TESTS_THAT_FAILED[@]}; do
		echo "    $fail"
	done
else
	echo "All tests PASSED."
fi

