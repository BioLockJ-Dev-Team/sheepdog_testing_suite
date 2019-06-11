# README

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


## How to use this test suite

Wherever you have this repository stored, saved that file path to your bash profile using the variable "SHEP".

For example, you might cd to this folder and run:
```
echo "export SHEP=$(pwd)" >> ~/.bash_profile
```
