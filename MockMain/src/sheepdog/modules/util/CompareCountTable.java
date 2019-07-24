package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CompareCountTable
{
	private static String[] levels = { "phylum", "class", "order", "family", "genus" };
	
	private static void assertEquals(File classificationFile, File biolockJInputFile, String level) throws Exception
	{
		
	}
	
	private static HashMap<String, Long> getExpectationMap(File inFile, String level)
		throws Exception
	{
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
					
					if( map.containsKey(foundTaxa))
						throw new Exception("Duplicate taxa " + foundTaxa);
					
					map.put(foundTaxa, Long.parseLong(sToken.nextToken()));
				}
			
			if( foundTaxa == null)
				throw new Exception("Could not find " + level + " " + s);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		File classificationFile = 
			new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\reparseKraken2Parser_2019Jul24\\01_Kraken2Parser\\output\\reparseKraken2Parser_2019Jul24_otuCount_SRR4454586.tsv");
		
		
		
		HashMap<String, Long> expectationMap = getExpectationMap(classificationFile, "genus");
		
		for(String s : expectationMap.keySet())
		{
			System.out.println(s + " " + expectationMap.get(s));
		}
	}
}
