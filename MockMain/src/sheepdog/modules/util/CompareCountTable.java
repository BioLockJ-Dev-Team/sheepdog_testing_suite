package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CompareCountTable
{
	private static String[] levels = { "phylum", "class", "order", "family", "genus" };
	
	private static void assertEquals(File classificationDirectory, File biolockJTable, String level) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(biolockJTable);
		
		HashMap<String, HashMap<String,Long>> expectationMaps = getExpectationMaps(classificationDirectory, level);
		
		if( wrapper.getSampleNames().size() != expectationMaps.size())
			throw new Exception("Unequal sample size " + wrapper.getSampleNames().size() + " " + expectationMaps.size());
		
	}
	
	/*
	 * Outer key is sample is;  inner key is taxa id
	 */
	private static HashMap<String, HashMap<String,Long>> getExpectationMaps(File classificationDirectory, String level) 
				throws Exception
	{
		HashMap<String, HashMap<String,Long>>  map = new HashMap<>();
		
		String[] inFiles = classificationDirectory.list();
		
		for(String s : inFiles)
		{
			if(! s.endsWith("metadata.tsv"))
			{
				String sampleId = s.substring(s.lastIndexOf("_")+1, s.length()).replace(".tsv", "");
				
				if( map.containsKey(sampleId))
					throw new Exception("Duplicate sample ID " + sampleId);
				
				File inFile = new File(classificationDirectory.getAbsoluteFile()  +File.separator + s);
				map.put(sampleId, getExpectationMap(inFile, level));
			}
		}
		
		return map;
	}
	
	private static HashMap<String, Long> getExpectationMap(File inFile, String level)
		throws Exception
	{
		System.out.println("Expectation map for " + inFile.getAbsolutePath() + " " + level);
		HashMap<String, Long> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader( new FileReader( inFile));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String foundTaxa = null;
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			if( sToken.countTokens() !=2 )
				throw new Exception("Expecting two tokens " + s );
			
			String[] splits =sToken.nextToken().split("\\|");
			
			for( String s2 : splits)
				if( s2.startsWith(level + "__") )
				{
					if( foundTaxa != null)
						throw new Exception("Duplicate levels" + level);
					
					foundTaxa = s2.replace(level + "__", "");
					
					Long oldVal = map.get(foundTaxa);
					
					if( oldVal == null)
						oldVal =0l;
					
					map.put(foundTaxa, Long.parseLong(sToken.nextToken()) + oldVal);
				}
			
			if( foundTaxa == null)
				throw new Exception("Could not find " + level + " " + s);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		File classificationDirectory= 
			new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\reparseKraken2Parser_2019Jul24\\01_Kraken2Parser\\output");
		
		for( String taxa : levels)
		{
			File inFile = 
					new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\reparseKraken2Parser_2019Jul24\\03_BuildTaxaTables\\output\\"+ 
								"reparseKraken2Parser_2019Jul24_taxaCount_" + taxa +  ".tsv");
			
			assertEquals(classificationDirectory, inFile, taxa);
			System.out.println("Pass " + taxa);
		}
		
		System.out.println("Global pass");
		
		/*
		 HashMap<String, HashMap<String,Long>> expectationMaps = 
				 getExpectationMaps(classificationDirectory, "genus");
		 
		 for(String s : expectationMaps.keySet())
			 System.out.println(s + " " + expectationMaps.get(s));
			 */
	}
}
