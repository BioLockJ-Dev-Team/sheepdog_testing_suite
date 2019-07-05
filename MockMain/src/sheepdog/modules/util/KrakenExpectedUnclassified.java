package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class KrakenExpectedUnclassified
{
	/*
	 * the key will be a full string such as 
	 * phylum__Bacteroidetes|class__Bacteroidia|order__Bacteroidales|family__Unclassified Bacteroidales Order|genus__Unclassified Bacteroidales
	 * 
	 * 
	 */
	public static HashMap<String, Long> getUnclassifiedMap( File inFile , String startLevel, String endLevel ) throws Exception
	{
		HashMap<String, Long> map = new LinkedHashMap<>();
		List<Holder> fileLines = getFileLines(inFile);
		
		for( int x=0; x < fileLines.size(); x++)
		{
			Holder h = fileLines.get(x);
			
			if( /* h.taxaLine.indexOf(startLevel + "__") != - 1 && */  // include kingdom level?
					 ! endAtLevel(h.taxaLine, endLevel)
					&& ! endsBelowLevel(h.taxaLine,endLevel))
			{
				long matchingSum =0;
				  
				for( int y=x+1; y < fileLines.size(); y++)
				{
					String candidateLine = fileLines.get(y).taxaLine;
					
					if( endAtLevel(candidateLine, endLevel) && candidateLine.indexOf(h.taxaLine) != -1 )
					{
						matchingSum += fileLines.get(y).taxaCount;
					}
				}
				
				if( matchingSum > h.taxaCount)
					throw new Exception("Parsing error");
				
				if( matchingSum < h.taxaCount)
					map.put(h.taxaLine, h.taxaCount - matchingSum);
			}

		}
		
		removeRedundant(map);
		
		return map;
	}
	
	private static void removeRedundant(HashMap<String, Long> map)
	{
		HashSet<String> toRemove =new HashSet<>();
		
		for(String s : map.keySet())
		{
			for(String s2 : map.keySet())
			{
				if( ! s.equals(s2) && s2.indexOf(s) != -1 )
					toRemove.add(s);
			}
		}
		
		for(String s : toRemove)
			map.remove(s);
	}
	
	private static  boolean endAtLevel(String s, String level)
	{
		int lastIndex =s.lastIndexOf(level + "__");
		
		if( lastIndex == -1)
			return false;
		
		s = s.substring(lastIndex, s.length());
		
		if( s.indexOf("|") == -1 )
			return true;
		
		return false;
	}
	
	private static boolean endsBelowLevel( String s, String level)
	{
		int lastIndex =s.lastIndexOf(level + "__");
		
		if( lastIndex == -1)
			return false;
		
		s = s.substring(lastIndex, s.length());
		
		if( s.indexOf("|") == -1 )
			return false;
		
		return true;
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
		
		HashMap<String, Long> map = getUnclassifiedMap(inFile, "p",  "g");
		
		for(String s : map.keySet())
		{
			System.out.println(s + " " + map.get(s));
		}
	}
}
