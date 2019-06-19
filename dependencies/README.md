# dependencies

All test pipelines require that you have BioLockJ installed.

Some pipelines reqire additional stuff.

### Software
Many biolockj modules are wrappers for external software.  See https://github.com/msioda/BioLockJ/wiki/Dependencies for a complete list of dependencies you may need and download links.

### properties files
For configurations that are specific to a single user (such as email, or local machine file paths) the individual tests use this:
`pipeline.defaultProps=NOT_IN_GIT_user.properties`

The file  `TEMPLATE_user.properties` lists the properties that might be expected.  Save a copy of the template as `NOT_IN_GIT_user.properties` in the same directory, edit to reflect filepaths on your machine.

### variables

`SHEP` always points to the top directory.                     
`SHEP_DATA` is used dynamicly. See the top level README.

Other variables that test might use are:<br>
`BLJ` - The local copy of the BioLockJ repository                     
`BLJ_PROJ` - The local destination for pipelines to be stored in

