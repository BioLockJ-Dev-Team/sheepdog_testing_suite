package sheepdog.modules;

import java.io.File;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModuleImpl;

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
	
		List<File> aList = getInputFiles();
		
		for( File aFile : aList)
			Log.info( getClass(), "GOT " + aFile.getAbsolutePath()  );
		
		Log.info( getClass(), "Exiting stub for execute task");

	}
	
}
