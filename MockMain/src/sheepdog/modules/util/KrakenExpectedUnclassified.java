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
	
	
	public static HashMap<String, Long> getUnclassifiedMap( List<Holder> fileLines , String startLevel, String endLevel ) throws Exception
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
					  
					for( int y=0; y < fileLines.size(); y++)
					{
						String candidateLine = fileLines.get(y).taxaLine;
						
						if( y != x &&  getLastLevel(candidateLine).equals(endLevel) && candidateLine.indexOf(h.taxaLine) != -1 )
						{
							matchingSum += fileLines.get(y).taxaCount;
							
							if( h.taxaLine.equals("phylum__Actinobacteria|class__Actinobacteria|order__Actinomycetales|family__Actinomycetaceae") 
									&& candidateLine.indexOf("Actinomycetaceae") != -1 )
							System.out.println("FOUND MATCH\t" + candidateLine + "\t"+  fileLines.get(y).taxaCount +"\t" + matchingSum);
						}
					}
					
					if( matchingSum > h.taxaCount)
						throw new Exception("Parsing error " + h.taxaLine + " " + matchingSum + " " + h.taxaCount);
					
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
	
	private static int getLowestLevel(HashMap<String, String> taxaMap) throws Exception
	{
		int x=0;
		
		for(String s: taxaMap.keySet())
			x= Math.max(x, getLevelIndex(s));
		
		return x;
	}
	
	private static List<String> getExpectedString(String inString, String firstLevel, String lastLevel, boolean forceToEnd) throws Exception
	{
		List<String> list = new ArrayList<>();
		//if( inString.indexOf("Crenarchaeota") != -1)
		//System.out.println("In " + inString);
		
		HashMap<String, String> taxaMap = getAsTaxaMap(inString);
	
		//if( inString.indexOf("Crenarchaeota") != -1)
			//System.out.println(taxaMap);
		
		if( taxaMap.size() == 0 )
			return list;
		
		StringBuffer buff =new StringBuffer();
		
		int startLevel = getLevelIndex(firstLevel);
		
		int endLevel = getLowestLevel(taxaMap);
		
		if(forceToEnd)
			endLevel= getLevelIndex(lastLevel);

		//if( inString.indexOf("Crenarchaeota") != -1)
		//System.out.println("Start " + startLevel + " END " + endLevel);
		
		String last = "";
		String lastTaxaString= "";
		
		//if( inString.indexOf("Crenarchaeota") != -1)
		//System.out.println("Start " + startLevel + " END " + endLevel);
		
		for( int x=startLevel; x <= endLevel; x++)
		{
			String taxa = taxaMap.get(TAXA_LEVELS[x]);
			boolean addToList = false;
			
			if(taxa == null)
			{
				taxa = "Unclassified " + last + " " + lastTaxaString;
				addToList =true;
			}
			else
			{
				last = taxa;
				lastTaxaString = TAXA_LEVELS_FIRST_UPPER[x];
			}
				
			if( x > startLevel)
				buff.append("|");
			
			buff.append( TAXA_LEVELS[x] + "__" +  taxa);
			
			if(addToList)
				list.add(buff.toString());
		}
		
		//if( inString.indexOf("Crenarchaeota") != -1)
		//	System.out.println("Out " + buff.toString());
		String s= buff.toString();
		
		if( ! list.contains(s))
			list.add(s);
		
		return list;
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
		boolean isTerminal;
	}
	
	private static String getLastLevel(String s)
	{
		int index= s.lastIndexOf("|");
		
		if( index == -1)
			return "";
		
		return s.substring(index+1, s.lastIndexOf("__"));
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
			
			//if( taxaString.indexOf("Unclassified") == -1 )
			{
				if( aVal != null)
				{
					if( ! countVal.equals(aVal))
						throw new Exception("Mismatch :" + taxaString+ " reparse:" +   aVal + " blj:" + countVal);
					//else
						//System.out.println("Match " + taxaString + " " +  aVal + " " + countVal);
				}
				else
				{
					throw new Exception("MISSED " + taxaString);
				}
			}
									
		}
	}

	/*
	 * When we create unclassified taxa, there can be duplicates
	 * if there are two unclassified taxa with the same parent taxa
	 * this function collapses the duplicates
	 */
	private static List<Holder> mergeList(List<Holder> list) throws Exception
	{
		HashMap<String, Long> map =new LinkedHashMap<>();
		
		for(Holder h : list)
		{
			Long aVal = map.get(h.taxaLine);
			
			if( aVal == null)
				aVal= 0L;
			
			aVal = aVal + h.taxaCount;
			
			map.put(h.taxaLine,aVal);
		}
		
		List<Holder> returnList= new ArrayList<>();
		
		for(String s : map.keySet())
		{
			Holder h = new Holder();
			h.taxaLine =s;
			h.taxaCount = map.get(s);
			returnList.add(h);
		}
		
		return returnList;
	}

	private static HashMap<String,Long> buildExpectationMap( List<Holder> fileLines, String startLevel, String endLevel ) throws Exception
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
		
		HashMap<String, Long> unclassifiedMap= getUnclassifiedMap(fileLines, startLevel, endLevel);
		
		for(String s : unclassifiedMap.keySet())
		{
			
			List<String> aList=  getExpectedString(s, startLevel, endLevel,true);
			String s2 = aList.get(aList.size()-1);
			
			if(map.containsKey(s2))
				if( s2.indexOf("Corynebacteriales") != -1)
				System.out.println("WARNING MAP CONTAINS " + s2 + " "+ map.get(s2));
			
			Long aVal = map.get(s2);
			
			if( aVal == null)
				aVal = 0l;
			
			map.put(s2, unclassifiedMap.get(s) + aVal);
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
			
			String aTaxa = splits[0];
			

			for( int x=0 ; x< TAXA_LEVELS.length; x++)
			{
				aTaxa= aTaxa.replace("|" + FIRST_CHARS[x] + "__", "|" +  TAXA_LEVELS[x] + "__");	
			}
			
			List<String> expecetdList = getExpectedString(aTaxa,startLevel,endLevel,false);
			long aVal = Long.parseLong(splits[1]);
			
			for( String s2 : expecetdList)
			{
				Holder h= new Holder();
				h.taxaLine=  s2;
				h.taxaCount = aVal;
				list.add(h);
			}
		}
		
		list =mergeList(list);
		
		for( int x=0; x < list.size(); x++)
		{
			Holder h = list.get(x);
			h.isTerminal = true;
			
			for( int y=0; h.isTerminal && y < list.size(); y++)
			{
				if( y != x && list.get(y).taxaLine.indexOf(h.taxaLine)  != -1)
					h.isTerminal = false;
			}
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
		System.out.println(getLastLevel("phylum__Actinobacteria|class__Actinobacteria|order__Actinomycetales|family__Actinomycetaceae|genus__Unclassified Actinomycetaceae"));

		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\input\\classifier\\kraken2\\urban_2files\\SRR4454586_reported.tsv");
		
		List<Holder> fileLines = getFileLines(inFile, "phylum", "genus");
		
		for(Holder h : fileLines)
		{
			if( h.taxaLine.indexOf("Actinomycetaceae") != -1)
			{
				System.out.println(h.taxaLine + " " + h.taxaCount + " " + h.isTerminal);
			}
		}
		
		HashMap<String, Long> uMap= getUnclassifiedMap(fileLines, "phylum", "genus");
		
		for(String s : uMap.keySet())
		{
			if( s.indexOf("Actinomycetaceae") != -1 )
			{
				System.out.println("uMap " + s + " " +   uMap.get(s));
				//System.out.println("uMapE " + s + " " +  getExpectedString(s, "phylum", "genus",true).get(0));
			}
		}
		
		
		HashMap<String, Long> map= buildExpectationMap(fileLines, "phylum", "genus");
		
		for(String s : map.keySet())
		{
			if( s.indexOf("Actinomycetaceae") != -1 )
			{
				System.out.println("map " + s + " " +   map.get(s));
			}
		}
		
		File biolockJFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\justKraken2Parser_2_2019Jul10\\01_Kraken2Parser\\output\\justKraken2Parser_2_2019Jul10_otuCount_SRR4454586.tsv");
	//	
		assertEquals(map, biolockJFile);
		
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
