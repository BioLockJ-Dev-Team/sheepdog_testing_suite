# Test bash layer

This folder holds tests code that executes between the point when the user calls `biolockj` and the point when the `BioLockJ` java program launches.

The tests in `testCommandLine.sh` do not actually run a pipeline.  They make use of the `BIOLOCKJ_TEST_MODE` variable to make the bash program exit before actually launching a pipeline.  The tests in basicRealTest.sh launch very slim pipelines, just enough to make sure that the bash scripts can actually launch a pipeline. Both are built to be very fast so a developer can run them after making each minimal change.

### First time

Run the `testCommandLine.sh` script. This will produce files in the output dir.  Those that have a corresponding file in the `expected` folder should match that file exactly.  Many of the outputs include file paths, which will be different for different machines.  Use the files that are checked-in in the `expected_examples` folder as a guide for what your output should look like.  Add your own files to the expected folder as you "approve" them by comparing them to the examples.  Git will ignore them.  In a few mintues, all of the tests in testCommandLine.sh should report "just as expected".

To do this in bulk:
```(bash)
cd $SHEP/test/bash
testCommandLine.sh
rm -rf expected
git checkout -- expected
mv expected/* output/.
mv output/* expected/.
testCommandLine.sh
```
This will move all of your file-path-specific outputs into the expected folder so they can be used as a reference for the testCommandLine.sh script, but the existing reference files that are checked in in git will be unaffected. `git status` should show no changes. When `testCommandLine.sh` runs again, everything should report "as expected".

### Use tests

During development or release testing, run testCommandLine.sh and basicRealTest.sh periodically.  

The `wrap_` scripts will save the test results to a file and note the latest git commit for both sheepdog and BioLockJ, making it easier to see how far back the last passing tests were.

### Update tests

Git is set to ignore new files in `expected` and `expected_examples`.  To add new ones, you will need to use `git add -f <filename>`.  Anytime you modifiy a file that is already checked in, git will notice automatically.
