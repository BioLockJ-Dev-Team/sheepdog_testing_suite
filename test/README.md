# Tests

For each environment, we need to run a complete set of full pipelines.
The folders for aws, cluster, docker and local contain full pipelines, designed to demonstrate that BioLockJ as a system is working correctly in that environment.

The module folder holds tests designed to get a comprehensive view of a single module or utility.  These are only created/used when developing or troubleshooting a particular module or utility and may not be run routinely.

The bash folder holds tests designed to make sure the bash layer is passing information correctly. It tests code that executes between the point when the user calls `biolockj` and the point when the BioLockJ java program launches.

## Routine Comprehensive Tests

The `run_*_testSet` documents are lists of tests that should be run before a release.  If time/resources permit, they should be run before each pull request.

**run_aws_testSets.txt**<br>
If you have aws set up (see [Using AWS](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/dependencies#using-aws) ), then these should all run and pass.

**run_bash_testSets.sh**<br>
If you are using an unix-like system, then these should all run and all return "just as expected" or "It completed".  You may need to add some of your files to define expectations, see [First time](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/test/bash#first-time).

**run_cluster_testSets.sh**<br>
This script includes cluster-specific tests as well as repeats of the local tests.

**run_docker_testSets.sh**<br>
If you have docker set up (see [Using Docker](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/dependencies#using-docker) ), then these should all run and pass.

**run_local_testSets.sh**<br>
These tests are extremely dependent on your local machine.  To run these tests, you will need to install each required [dependency](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/dependencies).  This script is written to run on the hypothetical computer where all dependencies have been handled. Realisticly, each developer will need to comment out the test sets that test modules that are not available on their machine.
