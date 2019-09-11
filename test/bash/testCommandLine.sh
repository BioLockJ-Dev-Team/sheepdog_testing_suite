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
	# we don't expect anything in the .err files
	errSize=$(du $OUT/${id}.err | cut -f 1)
	[ $errSize -gt 0 ] && echo "$(du -h $OUT/${id}.err)"
	[ $errSize -eq 0 ] && rm $OUT/${id}.err
	# compare the .out files to previous run
	hasDiff=$(diff $OUT/${id}.out $EXP/${id}.out || echo "comparison failed")
	[ ${#hasDiff} -gt 0 ] && echo "oh no! examine $id !"
	[ ${#hasDiff} -eq 0 ] && echo "$id --> just as expected."
	git --no-pager diff --no-index $EXP/${id}.out $OUT/${id}.out
}

OUT="$SHEP/test/bash/output"
EXP="$SHEP/test/bash/expected"
rm -rf $OUT
mkdir $OUT
echo "Output from individual tests are stored in: $OUT"

exampleConfig="configFile/example.properties"
examplePipeline="restartDir/examplePipeline_11_1969-07-16"

# full path
exampleConfigFP="$SHEP/test/bash/${exampleConfig}"
examplePipelineFP="$SHEP/test/bash/${examplePipeline}"

id=test_00
biolockj 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_01_v
biolockj -v 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_01_version
biolockj --version 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_02_h
biolockj -h 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_02_help
biolockj --help 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it


id=test_03_typo
biolockj -i 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_03_typos
biolockj -ih 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_03_longTypo
biolockj --aBadParam 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_04_f
biolockj -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_04_foreground
biolockj --foreground $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_04full_foreground
biolockj --foreground $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --foreground $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_05_basic
biolockj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


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
check_it

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
check_it

id=test_7full_docker
biolockj --docker $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


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


id=test_10_p
biolockj -p bar $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java -p bar $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_10_pass
biolockj --password bar $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --password  bar $exampleConfig 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_10_p_noArg1
biolockj -p -f $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_10_p_noArg2
biolockj -p $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_11_b
biolockj -b $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_11_blj
biolockj --docker --blj $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_11full_blj
biolockj --docker --blj $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --blj $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_12_bljsup
biolockj --docker --blj_sup $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_12full_bljsup
biolockj --docker --blj_sup $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --blj_sup $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_12full_bljsup_arg
biolockj --docker --blj_sup $SHEP $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --blj_sup $SHEP $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it


id=test_13_ext_mods
biolockj --external-modules $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_13full_ext_mods
biolockj --external-modules $SHEP/MockMain/dist $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_java --external-modules $SHEP/MockMain/dist $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it

id=test_13_ext_mods_noShort
biolockj -e $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_13_ext_mods_docker
biolockj --docker --external-modules $SHEP/MockMain/dist $exampleConfig 1> $OUT/${id}.out 2>$OUT/${id}.err
check_it

id=test_13full_ext_mods_docker
biolockj --docker --external-modules $SHEP/MockMain/dist $exampleConfigFP 1> $OUT/${id}.out 2>$OUT/${id}.err
launch_docker --docker --external-modules $SHEP/MockMain/dist $exampleConfigFP 1>> $OUT/${id}.out 2>>$OUT/${id}.err
check_it