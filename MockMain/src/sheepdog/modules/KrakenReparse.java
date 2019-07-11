package sheepdog.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;
import sheepdog.modules.util.KrakenExpectedUnclassified;

public class KrakenReparse extends BioModuleImpl
{
	private static final String GENUS_LEVEL = "genus";
	private static final String PHYLA_LEVEL = "phylum";

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
		
		
		Collection<File> pipelineInput = BioLockJUtil.getPipelineInputFiles();
		
		
		List<File> inputFiles = getInputFiles();
		
		for(File f : pipelineInput)
		{
			File matching = findMatching(f, inputFiles);
			assertEquals(f, matching);
		
		}
		
		Log.info( getClass(), "Exit stub for executeTask()");
		
	}
	
	private void assertEquals( File pipelineFile, File parserFile ) throws Exception
	{
		throw new Exception("Not implemented; ");
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
	
	private static HashMap<String, Long> getExpectedAtLevel(File file, String level) throws Exception
	{
		HashMap<String, Long> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s = reader.readLine();  s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits.length != 2)
			{
				throw new Exception("Expecting 2 tabbed tokens" + s);
			}
			
			String taxa = splits[0];
			taxa = taxa.substring(taxa.lastIndexOf("|") +1, taxa.length());
			
			if( taxa.startsWith(level + "__"))
			{
				taxa = taxa.replace(level + "__", "");
				
				if( map.containsKey(taxa))
					throw new Exception("Duplicate");
				
				map.put(taxa, Long.parseLong(splits[1]));
			}
		}
		
		reader.close();
		
		return map;
	}
	
	
}
