# Validation

Files used as input to the BioLockJ validation utility to confirm that the new run is the same as an older run.

The file structure under this directory matches the file structure under the top level sheepdog_testing_suite/validation folder.

If the variable `$SHEP` is pointed at the top level, test pipelines run using the top level input folder.  The top level validation folder has the expectations about the output that should be produced using those files.  If `$SHEP` points to the *big* subdirectory, then the big inputs are used, and this folder has the expectation files for the output.
