# Test the biolockj API

MOD=biolockj.module.report.taxa.AddPseudoCount
#MORE_MODS=/path/to/dir/with/MODS

# get a list of all modules
biolockj_api listModules 2> /dev/null

# get a list of modules that use the API_Module interface
biolockj_api listApiModules 2> /dev/null

# for each module that uses the interface, get a list of properties
biolockj_api listProps $MOD

MODS=$(biolockj_api listApiModules 2> /dev/null)
for m in $MODS; do
	echo "***  $m"
	biolockj_api listProps $m
done 

# Get a list of properties for the backbone
biolockj_api listProps

# Get a complete list of properties for the backbone and all modules
biolockj_api listAllProps  2> /dev/null

# Get all props including props for extra modules
#biolockj_api listAllProps $MORE_MODS

# get the description for a few properties from a module
biolockj_api describeProp pipeline.defaultSeqMerger $MOD # null
biolockj_api describeProp script.defaultHeader $MOD # text

# get the description for a few properties from the backbone
biolockj_api describeProp pipeline.defaultSeqMerger
biolockj_api describeProp pipeline.detachJavaModules

# get the format type for a few properties for an individual module
biolockj_api propType pipeline.detachJavaModules $MOD # Boolean
biolockj_api propType pipeline.defaultStatsModule $MOD # String
biolockj_api propType pipeleeene.Typo $MOD # null

# get the format type for a few properties for the backbone
biolockj_api propType pipeline.detachJavaModules # Boolean
biolockj_api propType pipeline.defaultStatsModule # String
biolockj_api propType pipeleeene.Typo # null

# test a prop/val pair for a module (one good, bad, null)
biolockj_api isValidProp script.numThreads 1 $MOD # true
biolockj_api isValidProp script.numThreads foo $MOD # false
biolockj_api isValidProp script.numTYPO 1 $MOD # null

# test a prop/val pair for the backbone (one good, bad, null)

# get the value for a given property given a config file

# get the value for a given property, with now config file (ie, defaults)
biolockj_api propValue script.numThreads

# get a json string with lots of this data using allProperties

# show help menu
biolockj_api help
# show help menu
biolockj_api 

# how many properties are listed in standard properties:
grep "\." $BLJ/resources/config/default/standard.properties | wc -l
# how many properties are listed using this API:
biolockj_api listAllProps  2> /dev/null | wc -l


allProps=$(biolockj_api listProps  2> /dev/null)
for prop in $allProps; do
	echo ""
	printf '%-15s %-55s\n' "property:" $prop
	type=$(biolockj_api propType $prop)
	printf '%-15s %-55s\n' "type:" "$type"
	desc=$(biolockj_api describeProp $prop)
	printf '%-15s %-55s\n' "description:" "$desc"
	default=$(biolockj_api propValue $prop)
	printf '%-15s %-55s\n' "default:" "$default"
done  
