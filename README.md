# sheepdog_testing_suite

The repository is designed to give [BioLockJ](https://github.com/msioda/BioLockJ) developers test pipelines (including the input data, configuration file, meta data file, etc) to verify the functionality of BioLockJ throughout the development cycle.

## What's here

**big**<br>
Contains an input folder with the same file structure as the top level input folder.

**input**<br>
Data files representing the various types of input that can be used with a BioLockJ pipeline.

**test**<br>
Configuration files to run test pipelines.

**validation**<br>
The output of the validation utility, this can be referenced by the properties files to ensure that the new output matches some previously established expectation.

**verification_tests**<br>
Tests designed to independently test that the output of one or more modules is *correct*. 

_Why no metadata folder?_<br>
Pipeline resources like metadata, primers, barcodes etc are either the 'real' files for a given dataset or they are custom-made variants for a particular test.  If they are real, they live with the data that they accurately describe; test pipelines that use those folders may have to ignore them as input files.  If they are made for a particular test, they live next to that test and they have a name that makes it obvious which test.properties file(s) they are there for.  

## How to use this test suite

Wherever you have this repository stored, save that file path to your bash profile using the variables `$SHEP` and `$SHEP_DATA`.

For example, you might cd to this folder and run:
```
echo "export SHEP=$(pwd)" >> ~/.bash_profile
echo "export SHEP_DATA=$(pwd)" >> ~/.bash_profile
```

`SHEP` always points to this top directory.

`SHEP_DATA` is used dynamicly.  It can point to this top directory (`SHEP_DATA=$SHEP`), OR it could point to the _big_ folder (`SHEP_DATA=$SHEP/big`).  This means that tests can be written for toy data sets, but developers can also set `SHEP_DATA=$SHEP/big` to say _"What if I had run this exact test on the full dataset?"_.  

To allow for this dynamic referencing, input files and validation files are referenced using `SHEP_DATA`, while other resources for a pipeline (ie metadata, barcodes, primers) are referenced using `SHEP`.

Other variables that test might use are:<br>
`BLJ` - The local copy of the BioLockJ repository
`BLJ_PROJ` - The local destination for pipelines to be stored in

