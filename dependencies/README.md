# Dependencies

_All test pipelines require that you have BioLockJ installed and are able to run it._

## properties files

For configurations that are specific to a single user (such as email or aws user info) the individual tests use this: <br>
`pipeline.defaultProps=${SHEP}/dependencies/NOT_IN_GIT_user.properties`

For configurations that are specific to a single machine (such as file paths for Rscript or the RDP jar file) the individual tests use this: <br>
`pipeline.defaultProps=${SHEP}/dependencies/NOT_IN_GIT_local.properties`

Unlike user.properties, **local.properties should NOT be used with docker or aws**.  In docker, configuring file paths for exacutables that are specific to your machine might override the paths for executables that are built into the docker containters.  In aws, the added configuration could result in uploading files / databases / programs that are unnecisary, thus costing time and money.

The file  `TEMPLATE_user.properties` lists the properties that might be expected.  Save a copy of the template as `NOT_IN_GIT_user.properties` in the same directory, likewise for `TEMPLATE_local.properties`. Edit the NOT_IN_GIT copy to reflect your user info, or filepaths on your machine.

```(bash)
cd $SHEP/dependencies
cp TEMPLATE_local.properties NOT_IN_GIT_local.properties
cp TEMPLATE_user.properties NOT_IN_GIT_user.properties
```

Avoid adding information that is not already listed.  If you need additional properties to make an _existing_ test work, that is a cue that something else is wrong.  If you need additional properties to make a _new_ test work, then we need to add that to the TEMPLATE file.  Individual tests should list (commented out) what properties they need from their defaultProps files.

## Variables

Files in this repository reference each other using the `$SHEP` variable.<br>
Less often, the dynamic `$SHEP_DATA` variable is used in the same way.

**Navigate to this folder** and run:<br>
(macOS)
```
cd sheepdog_testing_suite
echo "export SHEP=$(pwd)" >> ~/.bash_profile
echo 'export SHEP_DATA=$SHEP/data_small' >> ~/.bash_profile
source ~/.bash_profile
```
(Ubuntu)
```
cd sheepdog_testing_suite
echo "export SHEP=$(pwd)" >> ~/.bashrc
echo 'export SHEP_DATA=$SHEP/data_small' >> ~/.bashrc
source ~/.bashrc
```

`SHEP` always points to the top sheepdog_testing_suite directory.

`SHEP_DATA` is used dynamicly.  It can point to any of the `data_` folders: <br>
- `SHEP_DATA=$SHEP/data_small` OR 
- `SHEP_DATA=$SHEP/data_big` OR 
- `SHEP_DATA=$SHEP/data_tiny`.  

This means that tests can be written for toy data sets, but developers can also set `SHEP_DATA=$SHEP/data_big` to say _"What if I had run this exact test on the full size files?"_; or set `SHEP_DATA=$SHEP/data_tiny` to say _"I just want to check the plumbing really fast."_ 

To use this dynamic referencing, reference input files and validation files using `SHEP_DATA`. Other resources for a pipeline (ie metadata, barcodes, primers) should always use `SHEP`.  Any test that is not meant to be dynamic can use the static `SHEP` variable for everything.

Other variables that might be referenced in this repository:<br>
`BLJ` - The local copy of the BioLockJ repository                    
`BLJ_PROJ` - The local destination for pipelines to be stored in.
<br>These are set by running the BioLockJ `install` script.  You can override the defaults by setting the varibles in your profile _after_ the line that calls the blj_config script.

Check your current environment variables:
```(bash)
echo $SHEP
echo $SHEP_DATA
echo $BLJ
echo $BLJ_PROJ
```

## BioLockJ installation for developers

1. Fork the [BioLockJ](https://github.com/BioLockJ-Dev-Team/BioLockJ) repository.
1. Clone your fork to your local machine: `git clone https://github.com/<username>/BioLockJ`
1. Run the install script: `cd BioLockJ; ./install`
1. Build the jar file: `cd resources; ant`

Start a new terminal session and make sure that the variables exist and executables are on your `PATH`:<br>
```(bash)
echo $BLJ
echo $BLJ_PROJ
biolockj --help
```

The MockMain project in the test suite automates the process of running a set of test pipelines.  It uses the jar file.  You can check to see the most recent git commit when the jar file was built:<br>
`java -jar $BLJ/dist/BioLockJ.jar --version`<br>
returns: `BioLockJ v1.1 Build: v1.0-10-g4590ed3`<br>
The Build uses this format: `<most recent tag>-< # commits since that tag>-<commit hash>`<br>
This version and build number is recorded at the top of the results of the MockMain program. This allows you to verify that the changes you are testing are included in the build you are using for tests.

## Software

Many biolockj modules are wrappers for external software.  See [the Dependensies page in the BioLockJ wiki](https://github.com/msioda/BioLockJ/wiki/Dependencies) for a complete list of dependencies you may need and download links.

In most cases, the path to the executable will need to be configured in your [properties file](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies#properties-files).

### sra toolkit
If you want to download the full size data (to use the `big` directory), you will need the sra toolkit.

### ant
As a BioLockJ developer, you need to be able to build both BioLockJ and the MockMain testing program.  Both use ant.
<br>
(macOS)<br>
```(bash)
cd /Applications # wherever you want to install ant
ANT_DIST=apache-ant-1.9.14
curl -O http://apache.mirrors.lucidnetworks.net//ant/binaries/$ANT_DIST-bin.tar.bz2
tar xfj $ANT_DIST-bin.tar.bz2
cd $ANT_DIST
echo export ANT_HOME=$(pwd) >> ~/.bash_profile
echo export PATH=\$PATH:\$ANT_HOME/bin >> ~/.bash_profile
. ~/.bash_profile
```
(Ubuntu)<br>
```(bash)
apt-get update
apt-get install ant
```

## Using Eclipse

Some folders come with eclipse launch files as a convenience.  For these files to work, you will need to have "BioLockJ" open as a project in Eclipse and you will need to set the [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master#variables) in Eclipse.  The values for these variables must be entered in the Preferences for Eclipse on your machine with the values for your machine.  Additionally, launch files must be set up to look for those values.  Creating and storing new launch files is encouraged; please follow this system of using variables so that the launch files are usable by other developers.
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


## Using AWS

aws account
aws cli tools (command line interface)

TODO - put primary documentation in the BioLockJ wiki, reference it here but don't duplicate it.

### Using Docker
You can avoid installing any of BioLockJ's dependencies by using the docker containers,<br>
...but to do that you have to install docker.

TODO - put primary documentation in the BioLockJ wiki, reference it here but don't duplicate it.
