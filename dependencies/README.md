# dependencies

All test pipelines require that you have BioLockJ installed.

All test pipelines require a unix-like environment.

Some pipelines require additional stuff.

## Software

Many biolockj modules are wrappers for external software.  See [the Dependensies page in the BioLockJ wiki](https://github.com/msioda/BioLockJ/wiki/Dependencies) for a complete list of dependencies you may need and download links.

In most cases, the path to the executable will need to be configured in your [properties file](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies#properties-files).

### sra toolkit
If you want to download the full size data (to use the `big` directory), you will need the sra toolkit.

### Docker
You can avoid installing any of BioLockJ's dependencies by using the docker containers,<br>
...but to do that you have to install docker.

TODO

## properties files

For configurations that are specific to a single user (such as email, or local machine file paths) the individual tests use this:
`pipeline.defaultProps=NOT_IN_GIT_user.properties`

The file  `TEMPLATE_user.properties` lists the properties that might be expected.  Save a copy of the template as `NOT_IN_GIT_user.properties` in the same directory, edit to reflect filepaths on your machine.

## variables
So important that we put it at the top level!<br>
See [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables) in the top level README.

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


## AWS

aws account
aws cli tools (command line interface)

TODO
