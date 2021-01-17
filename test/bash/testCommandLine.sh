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
	# always use the generic version of the output
	OUT_FILE=$(${SHEP}/test/bash/generalize.sh $OUT/${id}.out)
	EXP_FILE=$EXP/${id}_generic.out
	#
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

id=test_1.00
biolockj 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.01_v
biolockj -v 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.01_version
biolockj --version 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.02_h
biolockj -h 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.02_help
biolockj --help 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.03_typo
biolockj -i 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.03_typos
biolockj -ih 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.03_longTypo
biolockj --aBadParam 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.04_f
biolockj -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.04_foreground
biolockj --foreground $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.04_fd
biolockj -fd $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.05_basic
biolockj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.6_r
biolockj -r $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.6_restart
biolockj --restart $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.6_restart_nonDir
biolockj --restart $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.7_d
biolockj -d $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.7_docker
biolockj --docker $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.7full_d
biolockj -d $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.7full_docker
biolockj --docker $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.8_a
biolockj -a $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws -a $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_1.8_aws
biolockj --aws $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_1.9_aws_g
biolockj --aws -g $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws -g $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_1.9_aws_gui
biolockj --aws --gui $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws --aws --gui $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_1.10_pass
biolockj --password bar $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.10_pass_noArg1
biolockj --password -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.10_pass_noArg2
biolockj --password $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.11_b
biolockj -b $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.11_blj
biolockj --docker --blj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.12_e
biolockj --docker -e SHEP=$SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.12_es
biolockj --docker -e SHEP=$SHEP,FOO=BAR $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.12_es_badForm
biolockj --docker -e SHEP=$SHEP,FOOBARBAZ $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.12_env-var
biolockj --docker --env-var SHEP=$SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.13_ext_mods
biolockj --external-modules $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.13_ext_mods_docker
biolockj --docker --external-modules $SHEP/MockMain/dist $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_1.14_g
biolockj -g $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.14_gui
biolockj --gui $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

#id=test_1.14_gui_noConfig
#biolockj --gui 1> $OUT/${id}.out 2>$OUT/${id}.err
#check_it

#id=test_1.14full_gui
#biolockj --gui $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
#launch_docker --gui $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
#check_it


id=test_1.15_w
biolockj -w $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.15_wait
biolockj --wait-for-start $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


rm -rf ${SHEP}/MockMain/pipelines/fastFail*

id=test_1.20_precheck
biolockj --precheck-only --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.20_p
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.20_rp
biolockj -rp $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.20_ap
biolockj -ap $examplePipeline 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_aws -ap $examplePipeline 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_1.24_u
biolockj -u $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
echo "" 1>> $OUT/${id}.out
echo "# With -u, the call to java should be identical with and without -p" 1>> $OUT/${id}.out 2>>$OUT/${id}.err
biolockj -up $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_1.24_ud
biolockj -du $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
echo "" 1>> $OUT/${id}.out 
echo "# With -u, the call to java should be identical with and without -p" 1>> $OUT/${id}.out 2>>$OUT/${id}.err
biolockj -dup $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_1.24_unused
biolockj --unused-props $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.24_ur
biolockj -ru $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_1.27_verbose
biolockj --verbose $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


# verify that bash times out in waiting, unless --wait-for-start is used
export BIOLOCKJ_TEST_MODE=""




# Less practical now that the wait time is very long by default.
# id=test_15full_longWait
# biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/longWait.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
# check_it
#
# id=test_15full_w_longWait
# biolockj --external-modules ${SHEP}/MockMain/dist -w ${SHEP}/test/bash/configFile/longWait.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
# check_it

rm -rf ${SHEP}/MockMain/pipelines/fastFail*

id=test_2.16full_fail
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it
sleep 1

id=test_2.16full_fail_docker
biolockj -e SHEP=$SHEP --blj -d --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it
sleep 1


id=test_2.17full_restart
biolockj -f --external-modules ${SHEP}/MockMain/dist \
	${SHEP}/test/bash/configFile/restartWithWait.properties 1> /dev/null 2>/dev/null
RESTART_DIR=$(last-pipeline)
echo "RESTART_DIR: $RESTART_DIR" 1> $OUT/${id}.out 2>$OUT/${id}.err
MASTER_PROP=$(ls $RESTART_DIR/MASTER*.properties)
echo "MASTER_PROP: $MASTER_PROP" 1>> $OUT/${id}.out
echo "configToFail.fail=N" >> $MASTER_PROP
biolockj --external-modules ${SHEP}/MockMain/dist \
	--restart $RESTART_DIR 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_2.18_jar_help
# note that the output of version and help is sent to std err, not std out
java -cp $BLJ/dist/BioLockJ.jar biolockj.BioLockJ --help 2> $OUT/${id}.out 1>$OUT/${id}.err
java -cp $BLJ/dist/BioLockJ.jar biolockj.BioLockJ -help 2>> $OUT/${id}.out 1>>$OUT/${id}.err
check_it

id=test_2.19_jar_version
java -cp $BLJ/dist/BioLockJ.jar biolockj.BioLockJ --version &> $OUT/${id}.out
java -cp $BLJ/dist/BioLockJ.jar biolockj.BioLockJ -version >> $OUT/${id}.out
TOTAL_TESTS=$((TOTAL_TESTS + 1))
[ $(grep "BioLockJ v" $OUT/${id}.out | wc -l ) -eq 2 ] \
  && [ $(grep "Build" $OUT/${id}.out | wc -l ) -eq 2 ] \
  && [ $(cat $OUT/${id}.out | wc -l ) -eq 2 ] \
  && PASSING_TESTS=$((PASSING_TESTS + 1))


rm -rf ${SHEP}/MockMain/pipelines/fastFail*
rm -rf ${SHEP}/MockMain/pipelines/configToFail*

id=test_2.20_precheck_repeats
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
# echo "# pipeline fails to form at all; show correct message." 1>> $OUT/${id}.out
# biolockj ${SHEP}/test/bash/configFile/failPipelineFormation.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it



id=test_2.21_cd-blj
rm -rf ${SHEP}/MockMain/pipelines/myFirstPipeline*
#alias cd-blj='cd $(last-pipeline); quick_pipeline_view'
biolockj -f $BLJ/templates/myFirstPipeline/myFirstPipeline.properties 1> /dev/null 2>/dev/null
cd $(last-pipeline); quick_pipeline_view  1> $OUT/${id}.out 2>$OUT/${id}.err
cd -
check_it


rm -rf ${SHEP}/MockMain/pipelines/fastFail*

id=test_2.25_unusedProps_repeats
biolockj -u --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1> $OUT/${id}.out 2>$OUT/${id}.err
sleep 1
echo "# new precheck pipeline replaces an old one by the same name (after failure)" 1>> $OUT/${id}.out
biolockj -u --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
sleep 1
echo "# standard pipeline replaces precheck pipeline (after failure)" 1>> $OUT/${id}.out
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
sleep 1
echo "# new precheck cannot replace a standard pipeline" 1>> $OUT/${id}.out
biolockj -u --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
sleep 1
echo "# standard pipeline replaces precheck pipeline" 1>> $OUT/${id}.out
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
sleep 1
echo "# new precheck cannot replace a standard pipeline" 1>> $OUT/${id}.out
biolockj -u --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it



id=test_2.26_noneUnused
biolockj -p ${SHEP}/test/bash/configFile/noneUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj -u ${SHEP}/test/bash/configFile/noneUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj ${SHEP}/test/bash/configFile/noneUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
check_it

id=test_2.26_showsUnused
echo "# With -p, the unused props are shown at the end of check dependencies" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj -p ${SHEP}/test/bash/configFile/hasUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
echo "" 1>> $OUT/${id}.out 
echo "# With -u, the unused props are shown at the end of check dependencies" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj -u ${SHEP}/test/bash/configFile/hasUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
echo "" 1>> $OUT/${id}.out 
echo "# With no args, the unused props are shown at the end of check dependencies" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj ${SHEP}/test/bash/configFile/hasUnused.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
check_it


rm -rf ${SHEP}/MockMain/pipelines/fastFail*

id=test_2.26_showsUnusedWithFailure
echo "# With -p, the unused props are not shown because there is a failure in check dependencies" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj -p --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
echo "" 1>> $OUT/${id}.out 
echo "# With -u, the unused props are shown (despite failing in check dependencies)" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj -u --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
echo "" 1>> $OUT/${id}.out 
echo "# With no args, the unused props are not shown because there is a failure in check dependencies" 1>> $OUT/${id}.out 2>> $OUT/${id}.err
biolockj --external-modules ${SHEP}/MockMain/dist ${SHEP}/test/bash/configFile/fastFail-multiModule.properties 1>> $OUT/${id}.out 2>> $OUT/${id}.err
check_it


rm -rf ${SHEP}/MockMain/pipelines/myFirstPipeline*

id=test_2.27_verbose
biolockj --verbose $BLJ/templates/myFirstPipeline/myFirstPipeline.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
WITH=$(last-pipeline)
grep pipeline.logLevel $WITH/MASTER* 1>> $OUT/${id}.out 2>>$OUT/${id}.err
BIG_NUM=$(grep DEBUG $WITH/myFirst*log | wc -l)
[ $BIG_NUM -gt 100 ] && echo "There are over 100 DEBUG lines with --verbose." 1>> $OUT/${id}.out 2>>$OUT/${id}.err
#
echo "---------" 1>> $OUT/${id}.out 2>>$OUT/${id}.err
#
biolockj $BLJ/templates/myFirstPipeline/myFirstPipeline.properties 1>> $OUT/${id}.out 2>>$OUT/${id}.err
WITHOUT=$(last-pipeline)
grep pipeline.logLevel $WITHOUT/MASTER* 1>> $OUT/${id}.out 2>>$OUT/${id}.err
SMALL_NUM=$(grep DEBUG $WITHOUT/myFirst*log | wc -l)
[ $SMALL_NUM -lt 10 ] && echo "There are fewer than 10 DEBUG lines without --verbose." 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


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
