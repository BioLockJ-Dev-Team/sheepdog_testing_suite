# MockMain User Guide

To get started, build the project.  Use the ant script in this directory by cd'ing into this directory and calling "ant". This takes a few seconds.

## Creating a testList file

See example testList: resources/exampleTestList.txt

## Running a testList

See example method of calling the MockMain jar file: resources/runExample.sh

## Creating validation files

See the example validation in ${SHEP}/tiny/validation/validationUtil/part2_validation_fromPart1
See https://github.com/msioda/BioLockJ/wiki/Validation

## Creating verification code

Create a module that will run after the module you want to validate.  Make your new module part of the MockMain project.  Make it find the module it tests and find that module input files and output files.  Write code to verify that those outputs meet your expectations given those inputs.
