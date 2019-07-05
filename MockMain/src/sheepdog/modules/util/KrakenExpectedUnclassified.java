package sheepdog.modules.util;

import java.io.File;
import java.util.HashMap;

public class KrakenExpectedUnclassified
{
	/*
	 * the key will be a full string such as 
	 * phylum__Bacteroidetes|class__Bacteroidia|order__Bacteroidales|family__Unclassified Bacteroidales Order|genus__Unclassified Bacteroidales
	 * 
	 * 
	 */
	public static HashMap<String, Long> getUnclassifiedMap( File inFile ) throws Exception
	{
		HashMap<String, Long> map = new HashMap<>();
		
		
		
		return map;
	}
	
	/*
	 * 
	 * Hard-coded file path for the development cycle here.
	 * Obviously, this main method will not be part of the automated test suite
	 */
	public static void main(String[] args) throws Exception
	{
		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\input\\classifier\\kraken2\\urban_2files\\SRR4454587_reported.tsv");
		
		HashMap<String, Long> map = getUnclassifiedMap(inFile);
		
		for(String s : map.keySet())
		{
			System.out.println(s + " " + map.get(s));
		}
	}
}
