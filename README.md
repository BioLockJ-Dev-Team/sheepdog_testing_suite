# sheepdog_testing_suite

The repository is designed to give [BioLockJ](https://github.com/msioda/BioLockJ) developers test pipelines (including the input data, configuration file, meta data file, etc) to verify the functionality of BioLockJ throughout the development cycle.

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

# How to use this test suite

1. (required) Clone or download this repository.
1. (required) Set your environment [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables).
1. (probably required) Create your `NOT_IN_GIT_user.properties` file following the instructions in [dependencies](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies#properties-files). (Not all tests use this file)
1. (optional) Set up launch files in [Eclipse](https://github.com/IvoryC/sheepdog_testing_suite#using-eclipse). 

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

When you make changes to BioLockJ (weather fixing a bug, refactoring the framwork or adding new features), you should run a testList.  Pick a comprehensive testList that runs full pipelines, or make a custom one to include more extensive testing of the componenents that might affected by your work.  Run the testList before you start working using the current master version of BioLockJ.  Run it periodically as you work.  Most importantly, run after you have finalized your changes, but before you submit the pull request to merge your work into the master branch.

If you create a new feature, make tests that prove your feature is working and add them to this repository so any future changes that break your feature are discovered quickly.

## Using Eclipse

Some folders come with eclipse launch files as a convenience.  For these files to work, you will need to have "BioLockJ" open as a project in Eclipse and you will need to set the [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables) in Eclipse.  The values for these variables must be entered in the Preferences for Eclipse on your machine with the values for your machine.  Additionally, launch files must be set up to look for those values.  Creating and storing new launch files is encouraged; please follow this system of using variables so that the launch files are usable by other developers.
<br>
<br>**To set the variables in Eclipes:**<br>
Eclipse > Preferences ...<br>
Run/Debug > String Substitution > New...<br>
_Enter the values for SHEP, SHEP_DATA, BLJ and BLJ_PROJ for your computer_<br>
<br>**To import the launch files into Eclipse:**<br>
File > Import ...<br>
Run/Debug > Launch Configurations > Next...<br>
Browse > _Select the folder containing the launch files_<br>
<br>_Any launch files stored in this repository should be set up to reference those variables,<br>
if you are making a new launch file, here is how you accomplish that:_
<br>**To make launch files that use variables:**<br>
Run > Run Configurations ...<br>
Java Application > _create or select the launch configuration_<br>
Environment > New ...<br>
Name: SHEP<br>
Value: ${SHEP}<br>
_Likewise for the other varibles_<br>
_Under the_ (x)=Arguments _tab, use variables in their fully dress form:_ ${SHEP}<br>
<br>**To Run or Debug using the Launch Files:**<br>
Run > Run Configurations ...<br>
Java Application > _select the launch configuration_ > Run
