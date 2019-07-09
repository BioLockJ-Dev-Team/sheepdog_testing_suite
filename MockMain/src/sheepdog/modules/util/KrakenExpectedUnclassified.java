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
	private static String[] TAXA_LEVELS = { "phylum" , "class" , "order" , "family" , "genus" } ;

	private static String[] TAXA_LEVELS_FIRST_UPPER = { "Phylum" , "Class" , "Order" , "Family" , "Genus" } ;
	private static String[] FIRST_CHARS = { "p" , "c", "o", "f", "g" };
	
	private static String addMissing(String s) throws Exception
	{

		List<Integer> missing = getMissingIndex(s);
		
		if( missing.size() == 0 )
			return s;
		
		return null;
	}
	
	private static List<Integer> getMissingIndex(String s )
	{
		String[] splits = s.split("|");
			
		int[] indexes = new int[splits.length];
		
		for( int x=0; x < indexes.length; x++)
			indexes[x] = getIndex(splits[x]);
		
		return null;
	}
	
	private static int getIndex(String aSplit)
	{
		for( int x=0; x < FIRST_CHARS.length; x++)
			if( aSplit.startsWith(FIRST_CHARS[x]))
				return x;
		
		return -1;
	}
	
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
		
		HashMap<String, Long> returnMap = new HashMap<>();
		
		for(String s : map.keySet())
		{
			String newKey = getExpectedString(s, endLevel);
			
			if( returnMap.containsKey(newKey))
				throw new Exception("Renaming collision error");
			
			returnMap.put(newKey, map.get(s));
		}
		
		return returnMap;
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
	
	private static String getExpectedString(String inString, String endLevel)
	{

		System.out.println(inString);
		inString = inString.substring(inString.indexOf("|")+1, inString.length());
		
		for( int x=0 ; x< TAXA_LEVELS.length; x++)
		{
			inString = inString.replace(FIRST_CHARS[x] + "__", TAXA_LEVELS[x] + "__");	
		}
		
		int startPos = inString.lastIndexOf("|");
		
		if( startPos == -1 )
			startPos = 0;
		
		String lastTaxa= inString.substring(startPos, inString.length());
		lastTaxa = lastTaxa.substring(lastTaxa.lastIndexOf("__") + 2, lastTaxa.length());
		
		String lastlevel = null;
		
		for( int x=0; x < TAXA_LEVELS.length; x++)
			if(  inString.indexOf(TAXA_LEVELS[x]+ "__") != -1 ) 
				lastlevel = TAXA_LEVELS_FIRST_UPPER[x];
		
		
			
		for( int x = 0 ; x < TAXA_LEVELS.length; x++)
		{
			if( inString.indexOf(TAXA_LEVELS[x]) == - 1 )
				inString = inString + "|" + TAXA_LEVELS[x] + "__Unclassified " + lastTaxa + " " + lastlevel;
		}
		
		return inString;
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
			
			// hack that will break for last levels other than genus; if it includes species don't include it in the list
			if( h.taxaLine.indexOf("s__") == -1 )
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
		/*
		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\input\\classifier\\kraken2\\urban_2files\\SRR4454586_reported.tsv");
		
		HashMap<String, Long> map = getUnclassifiedMap(inFile, "p",  "g");
		
		for(String s : map.keySet())
		{
			if( s.indexOf("Corynebacteriales") != -1)
				System.out.println(s + " " +  map.get(s) );
		}
		
		File biolockJFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\justKraken2Parser_2019Jul08\\01_Kraken2Parser\\output\\justKraken2Parser_2019Jul08_otuCount_SRR4454586.tsv");
		
		RdpExpectedUnclassified.assertUnclassifiedEquals(biolockJFile, map);
		*/
		
		String testString = "d__Bacteria|p__Actinobacteria|c__Actinobacteria|o__Corynebacteriales|g__Lawsonella|s__Lawsonella clevelandensis";
		
		String[] splits = testString.split("\\|");
		
		for( int x=0; x < splits.length; x++)
			System.out.println(splits[x] + " " + getIndex(splits[x]));
	}
	
	
}
