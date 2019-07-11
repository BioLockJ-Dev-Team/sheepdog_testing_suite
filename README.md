# sheepdog_testing_suite

The repository is designed to give [BioLockJ](https://github.com/msioda/BioLockJ) developers test pipelines (including the input data, configuration file, meta data file, etc) to verify the functionality of BioLockJ throughout the development cycle.

# How to use this test suite

## Quick Start

1. Get [BioLockJ](https://github.com/msioda/BioLockJ).  See [BioLockJ installation for developers](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/dependencies/README.md#biolockj-installation-for-developers).
1. Clone or download this repository.
1. Set your environment [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables).
1. Build the MockMain project, using the MockMain/build.xml file: `cd MockMain; ant`
1. Run the example test pipeline: `${SHEP}/MockMain/resources/runExample.sh`<br>See the MockMain documentation to learn more about what you see in the example and how to expand from it.

**Build up your reference pipelines**
1. (probably required) Create your `NOT_IN_GIT_user.properties` file following the instructions in [dependencies](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies#properties-files).<br>Not all tests use this file. Depending on the test you want to run, you may need to set up other [dependencies](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies) as well, so go ahead and skim that whole page.
1. Find another existing testList.  There is probably a script to run that test set.  Review the config files listed in that testList, and make sure you have the dependencies that the tests require. Run the test set.
1. Create a folder called `pipelines` next to the testList file.  This folder will be ignored by git. Its just for you.
1. Once the tests are done (and passing), move the pipelines you just generated from `$SHEP/MockMain/pipelines` to your new `pipelines` folder.  Assuming all tests passed, you now have a reference of pipelines that pass that test set.  
1. Select another testList and repeat.

**be an issue tracking master**
Along the way there will surely be times when BioLockJ fails because you did not set up something it needed. In those times, did BioLockJ give you an appropriate error message?  Did that info lead you to the solution?  If you encountered challenges, users will too! Be familiar with our [issue tracking](https://github.com/IvoryC/sheepdog_testing_suite/issues), and make sure these frustrations are in our issue collection.  Take a moment to review the tags, read a few issues, get familiar with the interface.  Look at newly created issues on a regular basis.


## What's here

**big**<br>
Contains an input folder with the same file structure as the top level input folder; same for validation. Unlike the top level input, these inputs are not reduced.  For sequences, the repository does not store the sequence files, but scripts to download the required sequence files.

**dependencies**<br>
Many pipelines require parameters or resources that have to be provided in a computer-specific way.  This folder has templates and instructions for dealing with these requirements.

**input**<br>
Data files representing the various types of input that can be used with a BioLockJ pipeline.

**MockMain**<br>
Java project that automates the testing process.

**test**<br>
Configuration files to run test pipelines.

**tiny**<br>
Contains an input folder with the same file structure as the top level input folder; same for validation.  Unlike the top level input, these are not necissarily big enough to be a good test.  These are designed to run **_fast_** to _'test the plumbing'_ before switching back to a more meaningful test.

**validation**<br>
The output of the validation utility, this can be referenced by the properties files to ensure that the new output matches some previously established expectation.

_Why no metadata folder?_<br>
Pipeline resources like metadata, primers, barcodes etc are either the 'real' files for a given dataset or they are custom-made variants for a particular test.  If they are real, they live with the data that they accurately describe; test pipelines that use those folders may have to ignore them as input files.  If they are made for a particular test, they live next to that test and they have a name that makes it obvious which test.properties file(s) they are there for.  

## Variables

Wherever you have this repository stored, save that file path to your bash profile using the variables `$SHEP` and `$SHEP_DATA`.

For example, you might **cd to this folder** and run:<br>
(macOS)
```
echo "export SHEP=$(pwd)" >> ~/.bash_profile
echo "export SHEP_DATA=$(pwd)" >> ~/.bash_profile
```
(Ubuntu)
```
echo "export SHEP=$(pwd)" >> ~/.bashrc
echo "export SHEP_DATA=$(pwd)" >> ~/.bashrc
```

`SHEP` always points to this top directory.

`SHEP_DATA` is used dynamicly.  It can point to this top directory (`SHEP_DATA=$SHEP`), OR it could point to the _big_ folder (`SHEP_DATA=$SHEP/big`) or the _tiny_ folder (`SHEP_DATA=$SHEP/tiny`).  This means that tests can be written for toy data sets, but developers can also set `SHEP_DATA=$SHEP/big` to say _"What if I had run this exact test on the full size files?"_; or set `SHEP_DATA=$SHEP/tiny` to say _"I just want to check the plumbing really fast."_ 

To allow for this dynamic referencing, input files and validation files are referenced using `SHEP_DATA`, while other resources for a pipeline (ie metadata, barcodes, primers) are referenced using `SHEP`.  Any test that is not meant to be dynamic can use the static `SHEP` variable for everything.

Other variables that might be referenced in this repository:<br>
`BLJ` - The local copy of the BioLockJ repository                    
`BLJ_PROJ` - The local destination for pipelines to be stored in.
<br>These are set by running the BioLockJ `install` script.  You can override the defaults by setting the varibles in your profile _after_ the line that calls the blj_config script.

## Automated testing

The MockMain project is a java program that takes a list of tests as input, runs BioLockJ for each test, and reports the output.  See MockMain [the MockMain Users Guide](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/MockMain/README.md#mockmain-user-guide) for more details on using the program.  In very very short: most folders of tests have a testList.txt table and a `runThisTestSet.sh` script.  Calling the script will run all the tests listed in the test list, and create a new table ( testList_results.txt ) giving the results of each test.  

When you make changes to BioLockJ (weather fixing a bug, refactoring the framwork or adding new features), you should run a testList.  Pick a comprehensive testList that runs full pipelines, or make a custom one to include more extensive testing of the componenents that might be affected by your work.  Run the testList before you start working using the current master version of BioLockJ.  Run it periodically as you work.  Most importantly, run after you have finalized your changes, but before you submit the pull request to merge your work into the master branch.

If you create a new feature, make tests that prove your feature is working and add them to this repository so any future changes that break your feature are discovered quickly.

