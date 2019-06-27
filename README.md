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

**verification_tests**<br>
Tests designed to independently test that the output of one or more modules is *correct*. 

_Why no metadata folder?_<br>
Pipeline resources like metadata, primers, barcodes etc are either the 'real' files for a given dataset or they are custom-made variants for a particular test.  If they are real, they live with the data that they accurately describe; test pipelines that use those folders may have to ignore them as input files.  If they are made for a particular test, they live next to that test and they have a name that makes it obvious which test.properties file(s) they are there for.  

# How to use this test suite

## Variables

Wherever you have this repository stored, save that file path to your bash profile using the variables `$SHEP` and `$SHEP_DATA`.

For example, you might cd to this folder and run:<br>
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

`SHEP_DATA` is used dynamicly.  It can point to this top directory (`SHEP_DATA=$SHEP`), OR it could point to the _big_ folder (`SHEP_DATA=$SHEP/big`).  This means that tests can be written for toy data sets, but developers can also set `SHEP_DATA=$SHEP/big` to say _"What if I had run this exact test on the full dataset?"_.  

To allow for this dynamic referencing, input files and validation files are referenced using `SHEP_DATA`, while other resources for a pipeline (ie metadata, barcodes, primers) are referenced using `SHEP`.

Other variables that test might use are:<br>
`BLJ` - The local copy of the BioLockJ repository                    
`BLJ_PROJ` - The local destination for pipelines to be stored in.
<br>These are set by running the BioLockJ `install` script.  You can override the defaults by setting the varibles in your profile _after_ the line that calls the blj_config script.

## Using Eclipse

Some folders come with eclipse launch files as a convenience.  For these files to work, you will need to have "BioLockJ" open as a project in Eclipse and you will need to set the [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables) in Eclipse.  <br>
<br>**To set the variables in Eclipes:**<br>
Eclipse > Preferences ...<br>
Run/Debug > String Substitution > New...<br>
<br>**To import the launch files into Eclipse:**<br>
File > Import ...<br>
Run/Debug > Launch Configurations > Next...<br>
Browse > _Select the folder containing the launch files_<br>
<br>**To Run or Debug using the Launch Files:**<br>
Run > Run Configurations ...
Java Application > _select the launch configuration_ > Run

## Automated testing

The MockMain project is a java program that takes a list of tests as input, runs BioLockJ for each test, and reports the output.  See MockMain README for more details on using the program.  In very very short: most folders of tests have a testList.txt table and a `runThisTestSet.sh` script.  Calling the script will run all the tests listed in the test list, and create a new table ( testList_results.txt ) giving the results of each test.  
