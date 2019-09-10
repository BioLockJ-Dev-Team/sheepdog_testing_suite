# Big Input

Directories used as inputs for BioLockJ test pipelines.

The file structure under this directory matches the file structure under the top level sheepdog_testing_suite/input folder.

UNLIKE the top level input directory, these are not toys.  These are the full size data, or at least "realisticly large".  In fact, the data are not actually stored in git.  What **_is_** stored in git is the scripts to download the data from public databases, and put the data in the right folders with the right names to match the top level input folder. While the top level input folder is good to go once it is cloned, this input folder has to be inflated before it is usefull.
