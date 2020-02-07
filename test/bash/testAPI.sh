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
echo "Output from individual tests are stored in: $OUT"
echo "Any pipelines formed will be stored in: $BLJ_PROJ"

MOD=biolockj.module.report.taxa.AddPseudoCount
MORE_MODS=$SHEP/MockMain/dist
CONFIG=$SHEP/bash/configFile/rarifySeqs.properties

# Test the biolockj API

# show help menu
id=api-test_00_noArgs
biolockj-api 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# show help menu
id=api-test_00_help
biolockj-api help 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get a list of all modules
id=api-test_01_listModules
biolockj-api listModules 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# list all modules, including extra modules in $MORE_MODS
id=api-test_02_listExtModules
biolockj-api listModules $MORE_MODS 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get a list of modules that use the API_Module interface
id=api-test_02_listApiModules
biolockj-api listApiModules 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get ApiModules including extra modules
id=api-test_03_listExtApiModules
biolockj-api listApiModules $MORE_MODS 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# for a module that uses the interface, get a list of properties
id=api-test_04_listProps_module
biolockj-api listProps $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# Get a list of properties for the backbone
id=api-test_05_listProps_backbone
biolockj-api listProps 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# Get a complete list of properties for the backbone and all modules
id=api-test_06_listAllProps
biolockj-api listAllProps 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# Get all props including props for extra modules
id=api-test_07_listAllExtProps
biolockj-api listAllProps $MORE_MODS  1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get the format type for a few properties for an individual module
 # integer
 # string
 # null for the module
 # null
id=api-test_08_propType_mod
biolockj-api propType script.numThreads $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err
biolockj-api propType cluster.batchCommand $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err
biolockj-api propType pipeline.defaultStatsModule $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err
biolockj-api propType pipeleeene.Typo $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get the format type for a few properties for the backbone
id=api-test_09_propType
biolockj-api propType pipeline.detachJavaModules 1> $OUT/${id}.out 2> $OUT/${id}.err # Boolean
biolockj-api propType pipeline.defaultStatsModule 1> $OUT/${id}.out 2> $OUT/${id}.err # String
biolockj-api propType pipeleeene.Typo 1> $OUT/${id}.out 2> $OUT/${id}.err # null
check_it g

# get the description for a few properties from a module
id=api-test_10_descProp_mod
biolockj-api describeProp pipeline.defaultSeqMerger $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err # null
biolockj-api describeProp script.defaultHeader $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err # text
check_it g

# get the description for a few properties from the backbone
id=api-test_11_descProp
biolockj-api describeProp pipeline.defaultSeqMerger 1> $OUT/${id}.out 2> $OUT/${id}.err # text
biolockj-api describeProp major.TYPO 1> $OUT/${id}.out 2> $OUT/${id}.err # text
biolockj-api describeProp pipeline.detachJavaModules 1> $OUT/${id}.out 2> $OUT/${id}.err # text
check_it g

# test a prop/val pair for a module (one good, bad, null)
id=api-test_12_isValidProp_mod
biolockj-api isValidProp script.numThreads 1 $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err # true
biolockj-api isValidProp script.numTYPO 1 $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err # null
check_it g

id=api-test_12_isValidProp_mod_msg
biolockj-api isValidProp script.numThreads foo $MOD 1> $OUT/${id}.out 2> $OUT/${id}.err # false
check_it g

# test a prop/val pair for the backbone (one good, bad, null)
id=api-test_13_isValidProp
biolockj-api isValidProp script.numThreads 1 1> $OUT/${id}.out 2> $OUT/${id}.err # true
biolockj-api isValidProp script.numTYPO 1 1> $OUT/${id}.out 2> $OUT/${id}.err # null
check_it g

id=api-test_13_isValidProp_msg
biolockj-api isValidProp script.numThreads apple 1> $OUT/${id}.out 2> $OUT/${id}.err # false (and error message)
check_it g

# test a prop/val pair given a config file
id=api-test_14_isValidConfigProp
#biolockj-api isValidConfigProp $CONFIG script.numThreads 

id=api-test_15_isValidConfigProp_msg
#biolockj-api isValidConfigProp $CONFIG script.numThreads

id=api-test_16_withConfig_noConfig_msg
#biolockj-api isValidConfigProp bad/config/path script.numThreads

# get the value for a given property, with no config file (ie, defaults)
id=api-test_17_propValue
biolockj-api propValue script.numThreads 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get the value for a given property given a config file
id=api-test_18_propValue_config
#biolockj-api propValue $CONFIG script.numThreads

# get a json string with lots of this data using the general properties
id=api-test_19_propInfo
biolockj-api propInfo 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get a json string with all info on all available modules
id=api-test_20_moduleInfo
biolockj-api moduleInfo 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g

# get a json string with all info on all modules including extra modules
id=api-test_21_moduleExtInfo
biolockj-api moduleInfo $MORE_MODS 1> $OUT/${id}.out 2> $OUT/${id}.err
check_it g
