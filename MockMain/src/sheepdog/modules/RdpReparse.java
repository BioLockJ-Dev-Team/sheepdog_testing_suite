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
	
		List<File> biolockJSummaryFiles= getInputFiles();
		
		for( File aFile : biolockJSummaryFiles)
			Log.info( getClass(), "GOT " + aFile.getAbsolutePath()  + " as biolockJ summary of RDP output" );
		
		if( biolockJSummaryFiles.size() ==0  )
			throw new Exception("No summary files");

		List<File> rdpOutput = 
				ModuleUtil.getPreviousModule(this).getInputFiles();
		
		for( File aFile : rdpOutput)
			Log.info(getClass(), "GOT " + aFile.getAbsolutePath() + " as RDP output file");
		
		if( rdpOutput.size() == 0 )
			throw new Exception("No RDP output");
		
		if( rdpOutput.size() != biolockJSummaryFiles.size())
			throw new Exception("Unequal numbers of files " + biolockJSummaryFiles.size()+ " " + rdpOutput.size());
		
		Log.info( getClass(), "Exiting stub for execute task");

		
	}
	
}
