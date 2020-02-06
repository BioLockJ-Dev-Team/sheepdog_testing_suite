# Test the biolockj API

MOD=biolockj.module.report.taxa.AddPseudoCount
MORE_MODS=$SHEP/MockMain/dist

# get a list of all modules
biolockj-api listModules 

# list all modules, including extra modules in $MORE_MODS
biolockj-api listModules $MORE_MODS

# get a list of modules that use the API_Module interface
biolockj-api listApiModules 

# get ApiModules including extra modules
biolockj-api listApiModules $MORE_MODS 

# for a module that uses the interface, get a list of properties
biolockj-api listProps $MOD

MODS=$(biolockj-api listApiModules )
for m in $MODS; do
	echo "***  $m"
	biolockj-api listProps $m
done 

# Get a list of properties for the backbone
biolockj-api listProps

# Get a complete list of properties for the backbone and all modules
biolockj-api listAllProps 

# Get all props including props for extra modules
biolockj-api listAllProps $MORE_MODS 

# get the format type for a few properties for an individual module
biolockj-api propType script.numThreads $MOD # integer
biolockj-api propType cluster.batchCommand $MOD # String
biolockj-api propType pipeline.defaultStatsModule $MOD # null for the module
biolockj-api propType pipeleeene.Typo $MOD # null

# get the format type for a few properties for the backbone
biolockj-api propType pipeline.detachJavaModules # Boolean
biolockj-api propType pipeline.defaultStatsModule # String
biolockj-api propType pipeleeene.Typo # null

# get the description for a few properties from a module
biolockj-api describeProp pipeline.defaultSeqMerger $MOD # null
biolockj-api describeProp script.defaultHeader $MOD # text

# get the description for a few properties from the backbone
biolockj-api describeProp pipeline.defaultSeqMerger # text
biolockj-api describeProp pipeline.detachJavaModules # text

# test a prop/val pair for a module (one good, bad, null)
biolockj-api isValidProp script.numThreads 1 $MOD # true
biolockj-api isValidProp script.numThreads foo $MOD # false
biolockj-api isValidProp script.numTYPO 1 $MOD # null

# test a prop/val pair for the backbone (one good, bad, null)
biolockj-api isValidProp script.numThreads 1 # true
biolockj-api isValidProp script.numThreads apple 


# get the value for a given property given a config file
biolockj-api

# get the value for a given property, with no config file (ie, defaults)
biolockj-api propValue script.numThreads

# get a json string with lots of this data using the general properties
biolockj-api propInfo

# get a json string with all info on all available modules
biolockj-api moduleInfo

# get a json string with all info on all modules including extra modules
biolockj-api moduleInfo $MORE_MODS

# show help menu
biolockj-api help
# show help menu
biolockj-api 

