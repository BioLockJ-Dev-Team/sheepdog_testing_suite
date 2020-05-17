package sheepdog.modules;

import java.io.File;
import java.util.Collection;
import java.util.List;

import biolockj.Constants;
import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;
import sheepdog.modules.util.KrakenExpectedUnclassified;

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
		
		List<File> inputFiles = getInputFiles();
		
		for(File f : pipelineInput)
		{
			File matching = findMatching(f, inputFiles);
			Log.info( getClass(), "HELLO: Compare " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
			//todo actually compre the files!
			
			Log.info( getClass(), "HELLO:  Pass " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
		}
		
		Log.info( getClass(), "Exit stub for executeTask() with global pass");
	
		
	}
	
	

	private static File findMatching(File pipelineFile, List<File> inputFiles) throws Exception
	{
		String sampleId = pipelineFile.getName();
		sampleId = sampleId.replace("_taxaCount", "").replace(".tsv", "");
		sampleId = sampleId + "_Shannon.tsv";
		
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
	
	
			
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}
	
}
