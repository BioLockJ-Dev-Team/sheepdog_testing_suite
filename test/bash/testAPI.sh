# Test the biolockj API

MOD=biolockj.module.report.taxa.AddPseudoCount
MORE_MODS=$SHEP/MockMain/dist

# get a list of all modules
biolockj_api listModules 2> /dev/null

# list all modules, including extra modules in $MORE_MODS
biolockj_api listModules $MORE_MODS

# get a list of modules that use the API_Module interface
biolockj_api listApiModules 2> /dev/null

# get ApiModules including extra modules
biolockj_api listApiModules $MORE_MODS 2> /dev/null

# for a module that uses the interface, get a list of properties
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
biolockj_api listAllProps $MORE_MODS 2> /dev/null

# get the format type for a few properties for an individual module
biolockj_api propType script.numThreads $MOD # integer
biolockj_api propType cluster.batchCommand $MOD # String
biolockj_api propType pipeline.defaultStatsModule $MOD # null for the module
biolockj_api propType pipeleeene.Typo $MOD # null

# get the format type for a few properties for the backbone
biolockj_api propType pipeline.detachJavaModules # Boolean
biolockj_api propType pipeline.defaultStatsModule # String
biolockj_api propType pipeleeene.Typo # null

# get the description for a few properties from a module
biolockj_api describeProp pipeline.defaultSeqMerger $MOD # null
biolockj_api describeProp script.defaultHeader $MOD # text

# get the description for a few properties from the backbone
biolockj_api describeProp pipeline.defaultSeqMerger # text
biolockj_api describeProp pipeline.detachJavaModules # text

# test a prop/val pair for a module (one good, bad, null)
biolockj_api isValidProp script.numThreads 1 $MOD # true
biolockj_api isValidProp script.numThreads foo $MOD # false
biolockj_api isValidProp script.numTYPO 1 $MOD # null

# test a prop/val pair for the backbone (one good, bad, null)
biolockj_api isValidProp script.numThreads 1 2> /dev/null # true
biolockj_api isValidProp script.numThreads apple 2> /dev/null 


# get the value for a given property given a config file

# get the value for a given property, with now config file (ie, defaults)
biolockj_api propValue script.numThreads

# get a json string with lots of this data using the general properties
biolockj_api propInfo 2> /dev/null

# get a json string with all info on all available modules
biolockj_api moduleInfo 2> /dev/null

# get a json string with all info on all modules including extra modules
biolockj_api moduleInfo $MORE_MODS

# show help menu
biolockj_api help
# show help menu
biolockj_api 

