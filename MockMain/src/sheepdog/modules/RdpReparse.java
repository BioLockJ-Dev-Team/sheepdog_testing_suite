package sheepdog.modules;

import java.io.File;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.ModuleUtil;

public class RdpReparse extends BioModuleImpl
{

	@Override
	public void checkDependencies() throws Exception
	{
		Log.info( getClass(), "IN stub for checkDependencies()");
	}
	

	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for execute task");
	
		List<File> rdpClassifierOutput= getInputFiles();
		
		for( File aFile : rdpClassifierOutput)
			Log.info( getClass(), "GOT " + aFile.getAbsolutePath()  + " as output file" );

		List<File> rdpClassifierInput = 
				ModuleUtil.getPreviousModule(this).getInputFiles();
		
		for( File aFile : rdpClassifierInput)
			Log.info(getClass(), "GOT " + aFile.getAbsolutePath() + " as input file");
		
		Log.info( getClass(), "Exiting stub for execute task");

		
	}
	
}
