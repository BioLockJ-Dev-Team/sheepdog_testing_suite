package sheepdog.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
	
	private void compareTwoMaps( HashMap<String, Double> map1, HashMap<String, Double> map2 ) throws Exception
	{
		Log.info( getClass(), "Checking maps " + map1 + " " + map2);
		
		if( ! map1.keySet().equals(map2.keySet()))
			throw new Exception("Unequal keys " + map1.keySet() + " " + map2.keySet());
		
		for(String s : map1.keySet())
		{
			Double val1 = map1.get(s);
			Double val2 = map2.get(s);
			
			if( val2 == null)
				throw new Exception("Logic error");
			
			if( Math.abs(val1 - val2) > 0.0001)
				throw new Exception("Disagreement " + s + " " + val1 + " " + val2);
		}
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for executeTask()");
		
		Collection<File> pipelineInput = BioLockJUtil.getPipelineInputFiles();
		
		List<File> inputFiles = getInputFiles();
		
		for(File f : pipelineInput)
		{
			HashMap<String, Double> originalMap = getDiversityMap(f);
			
			File matching = findMatching(f, inputFiles);
			Log.info( getClass(), "Compare " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
			HashMap<String, Double> biolockJMap = getOutputMap(matching);
			
			compareTwoMaps(biolockJMap, originalMap );
			
			Log.info( getClass(), "HELLO:  Pass " + f.getAbsolutePath() + " " + matching.getAbsolutePath());
			
		}
		
		Log.info( getClass(), "Exit stub for executeTask() with global pass");
		
	}
	

	private static File findMatching(File pipelineFile, List<File> inputFiles) throws Exception
	{
		String sampleId = pipelineFile.getName();
		
		File returnFile = null;
		
		for( File f : inputFiles)
		{
			String aName = f.getName().replace("_shannon", "");
			
			if( sampleId.indexOf(aName) != -1 )
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
	
	private static HashMap<String, Double> getOutputMap(File file)  throws Exception
	{
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("Unexpected line " + s + " " + file.getAbsolutePath());
			
			String key = splits[0];
			double val = Double.parseDouble(splits[1]);
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			map.put(key,val);
			
		}	
		
		reader.close();
		
		return map;
	}
	
	private static HashMap<	String, Double> getDiversityMap(File file) throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + file.getAbsolutePath());
			
			List<Double> counts = new ArrayList<Double>();
			
			double sum =0;
			
			for( int x=1; x < splits.length; x++)
			{
				Double aVal = Double.parseDouble(splits[x]);
				sum += aVal;
				counts.add(aVal);
			}
			
			double shannon = 0;
			
			for(Double d : counts)
			{
				double p = d / sum;
				
				if( p > 0 )
					shannon += p * Math.log(p);
			}
			
			if( shannon <  0 )
				map.put(key, -shannon);
			else
				map.put(key, 0.0);
		}
		
		reader.close();
		return map;
	}
	
	
			
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}
	
}
