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
		//Log.info( getClass(), "IN stub for checkDependencies()");
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for " + getClass().getName());
	
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
		
		for(File rdpFile : rdpOutput)
		{
			File biolockJSummary = findMatchingSummaryFile(rdpFile, biolockJSummaryFiles);
			Log.info(getClass(), "Getting ready to parse " + biolockJSummary.getAbsolutePath());
		}
		
		Log.info( getClass(), "Exiting RdpReparse module");
		
	}
	
	private static File findMatchingSummaryFile( File rdpOutputFile,  List<File> biolockJSummaryFiles) throws Exception
	{
		String sampleID = rdpOutputFile.getName().replace("_reported.tsv", "");
		
		File returnFile = null;
		
		for(File f : biolockJSummaryFiles)
		{
			if( f.getName().indexOf(sampleID) != - 1 )
			{
				if( returnFile != null)
					throw new Exception("Duplicate for " + sampleID);
				
				returnFile = f;
			}
		}
		
		if( returnFile == null)
			throw new Exception("Could not find " + sampleID );
		
		return returnFile;
		
	}
	
}
