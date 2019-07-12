package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;


public class KrakenExpectedUnclassified
{
	private static String[] TAXA_LEVELS = { "phylum" , "class" , "order" , "family" , "genus" , "species"} ;

	private static String[] TAXA_LEVELS_FIRST_UPPER = { "Phylum" , "Class" , "Order" , "Family" , "Genus", "Species" } ;
	private static String[] FIRST_CHARS = { "p" , "c", "o", "f", "g" , "s"};
	
	
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
		
		HashMap<String, String> taxaMap = getAsTaxaMap(inString);
		
		if( taxaMap.size() == 0 )
			return list;
		
		StringBuffer buff =new StringBuffer();
		
		int startLevel = getLevelIndex(firstLevel);
		
		int endLevel = getLowestLevel(taxaMap);
		
		if(forceToEnd)
			endLevel= getLevelIndex(lastLevel);
	
		String last = "";
		String lastTaxaString= "";
				
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
			
			if( aVal != null)
			{
				if( ! countVal.equals(aVal))
					throw new Exception("Mismatch :" + taxaString+ " reparse:" +   aVal + " blj:" + countVal);
				else
					System.out.println("Match " + taxaString + " " +  aVal + " " + countVal);
			}
			else
			{
				throw new Exception("MISSED " + taxaString);
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
			map.put(h.taxaLine, h.taxaCount);
			
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
			
			for( int x=0; x < expecetdList.size(); x++)
			{
				String s2 = expecetdList.get(x);
				Holder h= new Holder();
				h.taxaLine=  s2;
				h.taxaCount = aVal;
				
				if( x < expecetdList.size() -1 )
					h.isTerminal = false;
				else
					h.isTerminal = true;
				
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
		

		for( Holder h : list)
		{
			if( h.isTerminal && ! endAtLevel(h.taxaLine, endLevel) && ! endsBelowLevel(h.taxaLine, endLevel))
			{
				List<String> someList = getExpectedString(h.taxaLine, startLevel, endLevel, true);
				System.out.println("Replace " + h.taxaLine + " with " + someList.get(someList.size()-1));
				h.taxaLine = someList.get(someList.size()-1);
			}
		}
		
		List<Holder> newList = new ArrayList<>();
		
		for( Holder h : list)
			if( getLastLevel(h.taxaLine).equals(endLevel) || ( h.isTerminal && !endsBelowLevel(h.taxaLine, endLevel) ))
				newList.add(h);
		
		return newList;
	}
	
	private static void addUnclassifiedTaxaForALevel( HashMap<String, Long> map, String levelToAdd, String startLevel, String endLevel, File inFile )
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		HashMap<String, Long> toAdd = new HashMap<>();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			String taxaLine = splits[0];
			
			for( int x=0 ; x< TAXA_LEVELS.length; x++)
			{
				taxaLine= taxaLine.replace("|" + FIRST_CHARS[x] + "__", "|" +  TAXA_LEVELS[x] + "__");	
			}
			
			taxaLine = taxaLine.substring(taxaLine.indexOf("|") +1 , taxaLine.length());
			
			List<String> someList = getExpectedString(taxaLine, startLevel, endLevel,false);
			
			if( someList.size() != 0 )
				taxaLine = someList.get(someList.size()-1);
			
			Long count = Long.parseLong(splits[1]);
			if( getLastLevel(taxaLine).equals(levelToAdd))
			{		

				//if( levelToAdd.equals("family") &&  taxaLine.indexOf("Rhodothermaceae") != - 1)
				//{
					//System.out.println("START " + taxaLine + " " + count);
			//	}
				
				for(String s2 : map.keySet())
					if( s2.contains(taxaLine) )
					{
						
						count = count - map.get(s2);
					}
						
				if( count>0)
				{
					List<String> list = getExpectedString(taxaLine, startLevel, endLevel, true);
					String newTaxa= list.get(list.size()-1);
					
							
					Long oldCount = toAdd.get(newTaxa);
					
					if( oldCount == null)
						oldCount = 0l;
					
					toAdd.put(newTaxa, count + oldCount);
				}		
			}
		}
		
		for(String s : toAdd.keySet())
		{
			Long oldVal = map.get(s);
			
			if(oldVal == null)
				oldVal = 0L;
				
			map.put(s, toAdd.get(s) + oldVal);
		}
	}
	
	public static void assertEquals(File inFile, File biolockJFile) throws Exception
	{
		List<Holder> fileLines = getFileLines(inFile, "phylum", "genus");
		
		for(Holder h : fileLines)
		{
			if( h.taxaLine.indexOf("Parachlamydiales") != -1)
			{
				System.out.println(h.taxaLine + " " + h.taxaCount + " " + h.isTerminal);
			}
		}
		
		HashMap<String, Long> map = buildExpectationMap(fileLines, "genus", "phylum");
	
		 addUnclassifiedTaxaForALevel(map, "family","phylum", "genus", inFile);
			 addUnclassifiedTaxaForALevel(map, "order","phylum", "genus", inFile);
		 addUnclassifiedTaxaForALevel(map, "class","phylum", "genus", inFile);
		 addUnclassifiedTaxaForALevel(map, "phylum","phylum", "genus", inFile);		
		 
		assertEquals(map, biolockJFile);
		
	}
	
	/*
	 * 
	 * Hard-coded file path for the development cycle here.
	 * Obviously, this main method will not be part of the automated test suite
	 */
	public static void main(String[] args) throws Exception
	{	
		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\input\\classifier\\kraken2\\urban_2files\\SRR4454586_reported.tsv");
		File biolockJFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\justKraken2Parser_2019Jul11\\01_Kraken2Parser\\output\\justKraken2Parser_2019Jul11_otuCount_SRR4454586.tsv");
		assertEquals(inFile, biolockJFile);
		System.out.println("Pass");
	
	}
	
	
}
