package sheepdog.modules;

import java.io.File;
import java.util.Collection;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;
import sheepdog.modules.util.KrakenExpectedUnclassified;

public class KrakenReparse extends BioModuleImpl
{

	@Override
	public void checkDependencies() throws Exception
	{
		Log.info( getClass(), "IN stub for checkDependencies()");
	}
	
	@Override
	public void executeTask() throws Exception
	{
		
		Log.info( getClass(), "IN stub for executeTask()");
		
		
		Collection<File> pipelineInput = BioLockJUtil.getPipelineInputFiles();
		
		
		List<File> inputFiles = getInputFiles();
		
		for(File f : pipelineInput)
		{
			File matching = findMatching(f, inputFiles);
			Log.info( getClass(), "Compare " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
			KrakenExpectedUnclassified.assertEquals(f, matching);
		
			Log.info( getClass(), "Pass " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
		}
		
		Log.info( getClass(), "Exit stub for executeTask() with global pass");
		
	}
	
	
	private static File findMatching(File pipelineFile, List<File> inputFiles) throws Exception
	{
		String sampleId = pipelineFile.getName();
		sampleId = sampleId.substring(sampleId.lastIndexOf("/")+1, sampleId.length());
		sampleId = sampleId.replace("_reported.tsv", "");
		
		File returnFile = null;
		
		for( File f : inputFiles)
		{
			if( f.getName().indexOf(sampleId) != -1 )
			{
				if( returnFile != null)
					throw new Exception("Duplicate for " + sampleId);
				
				returnFile = f;
			}
		}
		
		if( returnFile == null)
			throw new Exception("Could not find match for " + pipelineFile.getAbsolutePath());
		
		return returnFile;
	}
		
	
}
