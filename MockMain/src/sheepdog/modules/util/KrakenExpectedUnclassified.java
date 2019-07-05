package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		List<Holder> fileLines = getFileLines(inFile);
		
		for( int x=0; x < fileLines.size(); x++)
		{
		}
		
		
		
		return map;
	}
	
	private static class Holder
	{
		String taxaLine;
		long taxaCount;
	}
	
	private static List<Holder> getFileLines( File inFile ) throws Exception
	{
		List<Holder> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("Expecting two tokens " + inFile.getAbsolutePath() +  " "+  s);
			
			Holder h= new Holder();
			h.taxaLine= splits[0];
			h.taxaCount = Long.parseLong(splits[1]);
			list.add(h);
		}
		
		return list;
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
