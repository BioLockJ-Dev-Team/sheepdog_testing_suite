package sheepdog.modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;

public class KrakenReparse extends BioModuleImpl
{
	//private static final String GENUS_LEVEL = "genus";

	@Override
	public void checkDependencies() throws Exception
	{
		Log.info( getClass(), "IN stub for checkDependencies()");
	}
	
	@Override
	public void executeTask() throws Exception
	{
		//todo: remove this comment
		
		Log.info( getClass(), "IN stub for executeTask()");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/mnt/c/temp/blah.txt")));
		
		
		Collection<File> pipelineInput = BioLockJUtil.getPipelineInputFiles();
		
		for(File f : pipelineInput)
			writer.write("pipeline : " + f.getAbsolutePath() + "\n");
		
		
		List<File> inputFile = getInputFiles();
		
		for( File f : inputFile)
			writer.write("input " +  f.getAbsolutePath() + "\n");
		
		writer.flush();  writer.close();
		
		Log.info( getClass(), "Exit stub for executeTask()");
		
	}
	
	
}
