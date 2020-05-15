package sheepdog.modules;

import java.io.File;
import java.util.Collection;

import biolockj.Constants;
import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;

public class CheckShannon extends BioModuleImpl
{
	@Override
	public void checkDependencies() throws Exception
	{

		Log.info( getClass(), "IN stub for checkDendencies()");
		
	}
	
	
	@Override
	public void executeTask() throws Exception
	{
		
		Log.info( getClass(), "IN stub for executeTask()");
		
		
		Collection<File> pipelineInput = BioLockJUtil.getPipelineInputFiles();
		
		for( File f : pipelineInput )
			Log.info( getClass(), "HELLO pipeline " + f.getAbsolutePath());
		
		for( File f : getInputFiles())
			Log.info(getClass(), "HELLO input " + f.getAbsolutePath());
		
		
		Log.info( getClass(), "Exit stub for executeTask() with global pass");
		
	}
			
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}
	
}
