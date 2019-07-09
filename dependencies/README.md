# dependencies

All test pipelines require that you have BioLockJ installed.

All test pipelines require a unix-like environment.

Some pipelines require additional stuff.

## Software

Many biolockj modules are wrappers for external software.  See [the Dependensies page in the BioLockJ wiki](https://github.com/msioda/BioLockJ/wiki/Dependencies) for a complete list of dependencies you may need and download links.

In most cases, the path to the executable will need to configured in your [properties file](https://github.com/IvoryC/sheepdog_testing_suite/tree/master/dependencies#properties-files).

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
See [variables](https://github.com/IvoryC/sheepdog_testing_suite/blob/master/README.md#variables) in the top level README.

## AWS

aws account
aws cli tools (command line interface)

TODO
