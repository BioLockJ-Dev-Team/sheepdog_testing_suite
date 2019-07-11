package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

public class KrakenExpectedUnclassified
{
	private static String[] TAXA_LEVELS = { "phylum" , "class" , "order" , "family" , "genus" , "species"} ;

	private static String[] TAXA_LEVELS_FIRST_UPPER = { "Phylum" , "Class" , "Order" , "Family" , "Genus", "Species" } ;
	private static String[] FIRST_CHARS = { "p" , "c", "o", "f", "g" , "s"};
	
	private static int getIndex(String s) throws Exception
	{
		for(int x=0; x < TAXA_LEVELS.length; x++)
			if( s.startsWith(TAXA_LEVELS[x] + "__"))
				return x;
		
		return -1;
	}
	
	
	/*
	 * the key will be a full string such as 
	 * phylum__Bacteroidetes|class__Bacteroidia|order__Bacteroidales|family__Unclassified Bacteroidales Order|genus__Unclassified Bacteroidales
	 * 
	 * 
	 */
	public static HashMap<String, Long> getUnclassifiedMap( List<Holder> fileLines , String endLevel ) throws Exception
	{
		HashMap<String, Long> map = new LinkedHashMap<>();
		
		for( int x=0; x < fileLines.size(); x++)
		{
			Holder h = fileLines.get(x);
			
			if( h.taxaLine != null)
			{

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

		}
		
		return map;	
	}
	
	private static String getATaxa(String in, String level) throws Exception
	{
		String[] splits = in.split("\\|");
		
		for(String s : splits)
			if( s.startsWith(level+ "__"))
				return s.replace(level + "__", "");
		
		return null;
	}
	
	private static HashMap<String, String> getAsTaxaMap(String in) throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<>();
		
		for( int x=0; x < TAXA_LEVELS.length; x++)
		{
			String aTaxa = getATaxa(in, TAXA_LEVELS[x]);
			
			if( aTaxa != null)
				map.put(TAXA_LEVELS[x], aTaxa);
		}
			
		return map;
	}
	
	private static int getLevelIndex(String s) throws Exception
	{
		for( int x=0; x < TAXA_LEVELS.length; x++)
			if( TAXA_LEVELS[x].equals(s))
				return x;
		
		throw new Exception("Could not find " +  s);
	}
	
	private static String getExpectedString(String inString, String firstLevel, String lastLevel) throws Exception
	{
		System.out.println("In " + inString);
		
		for( int x=0 ; x< TAXA_LEVELS.length; x++)
		{
			inString = inString.replace("|" + FIRST_CHARS[x] + "__", "|" +  TAXA_LEVELS[x] + "__");	
		}
		
		HashMap<String, String> taxaMap = getAsTaxaMap(inString);
		
		if( taxaMap.size() == 0 )
			return null;
		
		StringBuffer buff =new StringBuffer();
		
		int startLevel = getLevelIndex(firstLevel);
		
		int endLevel = getLevelIndex(lastLevel);
		
		// walk down to find the end of the input string
		if( endLevel == -1 )
		{
			for( int x= startLevel; x <= endLevel -1; x++  )
			{
				int aLevel = getIndex(TAXA_LEVELS[x]);
				
				if( aLevel != -1)
					endLevel = aLevel;
			}
		}
		
		
		String last = "";
		String lastTaxaString= "";
		
		for( int x=startLevel; x <= endLevel; x++)
		{
			String taxa = taxaMap.get(TAXA_LEVELS[x]);
			
			if(taxa == null)
			{
				taxa = "Unclassified " + last + " " + lastTaxaString;
			}
			else
			{
				last = taxa;
				lastTaxaString = TAXA_LEVELS_FIRST_UPPER[x];
			}
				
			if( x > startLevel)
				buff.append("|");
			
			buff.append( TAXA_LEVELS[x] + "__" +  taxa);
		}
		
		System.out.println("Out " + buff.toString());
		return buff.toString();
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
	
	private static void assertEquals( HashMap<String,Long> expectationMap, File biolockJOuputFile ) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(biolockJOuputFile));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s,"\t");
			
			if( sToken.countTokens() != 2)
				throw new Exception("Expecting 2 tabbed tokens " + sToken.countTokens() + " " +   s);
			
			String taxaString = sToken.nextToken();
			Long countVal = Long.parseLong(sToken.nextToken());
			
			Long aVal = expectationMap.get(taxaString);
			
			if( aVal != null)
			{
				if( ! countVal.equals(aVal))
					System.out.println("Mismatch " + taxaString+ " " +   aVal + " " + countVal);
				else
					System.out.println("Match " + taxaString + " " +  aVal + " " + countVal);
			}
			else
			{
				throw new Exception("MISSED " + taxaString);
			}						
		}
	}


	private static HashMap<String,Long> buildExpectationMap( List<Holder> fileLines, String endLevel ) throws Exception
	{
		HashMap<String,Long> map = new LinkedHashMap<>();
		
		for(Holder h : fileLines)
		{
			if( h.taxaLine != null && ! endsBelowLevel(h.taxaLine, endLevel))
			{

				Long oldVal = map.get(h.taxaLine);
				
				if(oldVal == null)
					oldVal =0l;
				
				map.put(h.taxaLine, Math.max(oldVal, h.taxaCount));
			}
			
		}
		
		HashMap<String, Long> unclassifiedMap= getUnclassifiedMap(fileLines, endLevel);
		
		for(String s : unclassifiedMap.keySet())
		{
			if( map.containsKey(s))
				throw new Exception("Duplicate " + s);
			
			map.put(s, unclassifiedMap.get(s));
		}
		
		return map;
	}
	
	private static List<Holder> getFileLines( File inFile, String startLevel, String endLevel ) throws Exception
	{
		List<Holder> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("Expecting two tokens " + inFile.getAbsolutePath() +  " "+  s);
			
			Holder h= new Holder();
			h.taxaLine=  getExpectedString(splits[0],startLevel,endLevel);
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
		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\input\\classifier\\kraken2\\urban_2files\\SRR4454586_reported.tsv");
		
		List<Holder> fileLines = getFileLines(inFile, "phylum", "genus");
		
		HashMap<String,Long> expectationMap = buildExpectationMap(fileLines, "genus");
		
		File biolockJFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\justKraken2Parser_2_2019Jul10\\01_Kraken2Parser\\output\\justKraken2Parser_2_2019Jul10_otuCount_SRR4454586.tsv");
		
		assertEquals(expectationMap, biolockJFile);
		
	//	for( Holder h : fileLines)
		//	System.out.println(h.taxaLine);
		
	//	String s= 
		//"d__Archaea|p__Crenarchaeota|c__Thermoprotei|o__Sulfolobales|f__Sulfolobaceae|g__Saccharolobus|s__Saccharolobus solfataricus";
		
		//System.out.println(removeJumps(s));
		
		/*
		HashMap<String, Long> map = getUnclassifiedMap(inFile, "p",  "g");
		
		for(String s : map.keySet())
		{
			if( s.indexOf("Verrucomicrobia") != -1)
				System.out.println(s + " " +  map.get(s) );
		}
		
		File biolockJFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\justKraken2Parser_2_2019Jul10\\01_Kraken2Parser\\output\\justKraken2Parser_2_2019Jul10_otuCount_SRR4454586.tsv");
		
		RdpExpectedUnclassified.assertUnclassifiedEquals(biolockJFile, map);
		
		/*
		
		String testString = "d__Bacteria|p__Actinobacteria|c__Actinobacteria|o__Corynebacteriales|g__Lawsonella|s__Lawsonella clevelandensis";
		
		String[] splits = testString.split("\\|");
		
		for( int x=0; x < splits.length; x++)
			System.out.println(splits[x] + " " + getIndex(splits[x]));
			*/
	}
	
	
}
