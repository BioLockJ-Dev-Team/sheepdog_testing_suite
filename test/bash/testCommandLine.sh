#! /bin/bash

# This script does not require any arguments
#
# For repeated testing during active development, 
# use the wrap_testCommandLine.sh.
#
# This test assumes that BioLockJ has been installed
# and that (as a result) the biolockj script directory
# is on the $PATH, as we expect it to be for users.

# This variable is a queu to the biolockj scripts that we are in test mode
# The script should reach a significant command (calling another script, or docker, or java)
# and instead of executing the command, they should print the command and exit.
export BIOLOCKJ_TEST_MODE="KEY CMD: "

check_it(){
	echo "-"
	TOTAL_TESTS=$((TOTAL_TESTS + 1))
	#
	# we don't expect anything in the .err files
	errSize=$(du $OUT/${id}.err | cut -f 1)
	[ $errSize -gt 0 ] && echo "$(du -h $OUT/${id}.err)"
	[ $errSize -eq 0 ] && rm $OUT/${id}.err
	#
	# compare the .out files to previous run.
	# if $1=="g" (for "generic") then use 
	# the generic version of the output
	if [ $# -gt 0 ] && [ $1 == "g" ] ; then
		OUT_FILE=$(${SHEP}/test/bash/generalize.sh $OUT/${id}.out)
		EXP_FILE=$EXP/${id}_generic.out
	else
		OUT_FILE=$OUT/${id}.out
		EXP_FILE=$EXP/${id}.out
	fi
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

OUT="$SHEP/test/bash/output"
EXP="$SHEP/test/bash/expected"
rm -rf $OUT
mkdir $OUT
export BLJ_PROJ="${SHEP}/MockMain/pipelines"
rm -rf ${SHEP}/MockMain/pipelines/test_*
rm -rf ${SHEP}/MockMain/pipelines/example*
rm -rf ${SHEP}/MockMain/pipelines/longWait*
rm -rf ${SHEP}/MockMain/pipelines/fastFail*
rm -rf ${SHEP}/MockMain/pipelines/restart*
echo "Output from individual tests are stored in: $OUT"

exampleConfig="configFile/example.properties"
examplePipeline="restartDir/examplePipeline_11_1969-07-16"

# full path
exampleConfigFP="$SHEP/test/bash/${exampleConfig}"
examplePipelineFP="$SHEP/test/bash/${examplePipeline}"

id=test_00
biolockj 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g


id=test_01_v
biolockj -v 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_01_version
biolockj --version 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g


id=test_02_h
biolockj -h 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_02_help
biolockj --help 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g


id=test_03_typo
biolockj -i 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_03_typos
biolockj -ih 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_03_longTypo
biolockj --aBadParam 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_04_f
biolockj -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_04_foreground
biolockj --foreground $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_04full_foreground
biolockj --foreground $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --foreground $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_04full_fd
biolockj -fd $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker -fd $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_05_basic
biolockj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_6_r
biolockj -r $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java -r $examplePipeline 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_6_restart
biolockj --restart $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --restart $examplePipeline 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_6_restart_nonDir
biolockj --restart $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --restart $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_7_d
biolockj -d $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_7_docker
biolockj --docker $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

# the results here include a lot of computer specific file paths
id=test_7full_d
biolockj -d $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker -d $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_7full_docker
biolockj --docker $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_8_a
biolockj -a $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws -a $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_8_aws
biolockj --aws $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_9_aws_g
biolockj --aws -g $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws -g $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_9_aws_gui
biolockj --aws --gui $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws --gui $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_10_pass
biolockj --password bar $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --password  bar $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_10_pass_noArg1
biolockj --password -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_10_pass_noArg2
biolockj --password $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g


id=test_11_b
biolockj -b $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_11_blj
biolockj --docker --blj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_11full_blj
biolockj --docker --blj $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --blj $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_12_e
biolockj --docker -e SHEP=$SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_12_env-var
biolockj --docker --env-var SHEP=$SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_12full_env-var
biolockj --docker -e SHEP=$SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker -e SHEP=$SHEP $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_13_ext_mods
biolockj --external-modules $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_13full_ext_mods
biolockj --external-modules $SHEP/MockMain/dist $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --external-modules $SHEP/MockMain/dist $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_13_ext_mods_docker
biolockj --docker --external-modules $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_13full_ext_mods_docker
biolockj --docker --external-modules $SHEP/MockMain/dist $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --external-modules $SHEP/MockMain/dist $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_14_g
biolockj -g $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_14_gui
biolockj --gui $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_14_gui_noConfig
biolockj --gui 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

#id=test_14full_gui
#biolockj --gui $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
#launch_docker --gui $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
#check_it


id=test_15_w
biolockj -w $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_15_wait
biolockj --wait-for-start $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_15full_w
biolockj -w $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java -w $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g



id=test_20_precheck
biolockj --precheck-only --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --precheck-only --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_20_p
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_20_rp
biolockj -rp $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java -rp $examplePipeline 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_20_ap
biolockj -ap $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws -ap $examplePipeline 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g





# verify that bash times out in waiting, unless --wait-for-start is used
export BIOLOCKJ_TEST_MODE=""
. $BLJ/script/blj_functions





id=test_15full_longWait
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/longWait.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_15full_w_longWait
biolockj --external-modules ${SHEP}/MockMain/dist -w ${SHEP}/test/bash/configFile/longWait.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g

id=test_16full_fail
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g
sleep 1

id=test_16full_fail_docker
biolockj -e SHEP=$SHEP --blj -d --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it g
sleep 1


id=test_17full_restart
biolockj --external-modules ${SHEP}/MockMain/dist \
	${SHEP}/test/bash/configFile/restartWithWait.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
RESTART_DIR=$(most_recent_pipeline)
echo "RESTART_DIR: $RESTART_DIR" 1>> $OUT/${id}.out
MASTER_PROP=$(ls $RESTART_DIR/MASTER*.properties)
echo "MASTER_PROP: $MASTER_PROP" 1>> $OUT/${id}.out
echo "configToFail.fail=N" >> $MASTER_PROP
biolockj --external-modules ${SHEP}/MockMain/dist \
	--restart $RESTART_DIR 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g

id=test_18_jar_help
# note that the output of version and help is sent to std err, not std out
java -jar $BLJ/dist/BioLockJ.jar --help 2> $OUT/${id}.out 1>$OUT/${id}.err
java -jar $BLJ/dist/BioLockJ.jar -help 2>> $OUT/${id}.out 1>>$OUT/${id}.err
check_it g

id=test_19_jar_version
java -jar $BLJ/dist/BioLockJ.jar --version &> $OUT/${id}.out
java -jar $BLJ/dist/BioLockJ.jar -version >> $OUT/${id}.out
TOTAL_TESTS=$((TOTAL_TESTS + 1))
[ $(grep "BioLockJ v" $OUT/${id}.out | wc -l ) -eq 2 ] \
  && [ $(grep "Build" $OUT/${id}.out | wc -l ) -eq 2 ] \
  && [ $(cat $OUT/${id}.out | wc -l ) -eq 2 ] \
  && PASSING_TESTS=$((PASSING_TESTS + 1))


id=test_20_precheck_repeats
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
echo "# new precheck pipeline replaces an old one by the same name (after failure)" 1>> $OUT/${id}.out
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# standard pipeline replaces precheck pipeline (after failure)" 1>> $OUT/${id}.out
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# new precheck cannot replace a standard pipeline" 1>> $OUT/${id}.out
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# standard pipeline replaces precheck pipeline" 1>> $OUT/${id}.out
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# new precheck cannot replace a standard pipeline" 1>> $OUT/${id}.out
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# new precheck with different name creates a new folder" 1>> $OUT/${id}.out
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/configToFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# new precheck replaces old one with the same name (after success)" 1>> $OUT/${id}.out
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/configToFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# standard pipeline replaces precheck pipeline (after success)" 1>> $OUT/${id}.out
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/configToFail.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
echo "# pipeline fails to form at all; show correct message." 1>> $OUT/${id}.out
biolockj ${SHEP}/test/bash/configFile/failPipelineFormation.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


id=test_21_cd-blj
biolockj $BLJ/templates/myFirstPipeline/myFirstPipeline.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
cd-blj  1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it g


echo ""
echo "Ran $TOTAL_TESTS tests on bash command line args."
if [ $TOTAL_TESTS -gt $PASSING_TESTS ]; then
	numFailed=$((TOTAL_TESTS - PASSING_TESTS))
	echo "There were $numFailed tests that FAILED !!!"
	exit 1
else
	echo "All $PASSING_TESTS tests PASS."
	exit 0
fi
