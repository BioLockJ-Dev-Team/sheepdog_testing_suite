package sheepdog.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import sheepdog.modules.RdpReparse;


public class RdpExpectedUnclassified
{
	private static int getIndex(String s, String[] a) throws Exception
	{
		for( int x=0; x < a.length; x++)
			if( a[x].equals(s))
				return x;
		
		throw new Exception("Could not find " + s);
	}
	
	private static String[] TAXA_LEVELS = { "phylum" , "class" , "order" , "family" , "genus" } ;
	private static String[] TAXA_LEVELS_FIRST_UPPER = { "Phylum" , "Class" , "Order" , "Family" , "Genus" } ;
	
	/*
	 * the key will be a full string such as 
	 * phylum__Acidobacteria|class__Acidobacteria_Gp1|order__Unclassified Acidobacteria_Gp1 Class|family__Unclassified Acidobacteria_Gp1 Class|genus__Unclassified Acidobacteria_Gp1 Class
	 */
	public static HashMap<String, Long> getUnclassifiedMap( File inFile , String startLevel, String endLevel ) throws Exception
	{
		HashMap<String, Long> map = new LinkedHashMap<>();
		int startIndex = getIndex(startLevel, TAXA_LEVELS);
		int endIndex = getIndex(endLevel, TAXA_LEVELS);
	
		List<NewRDPParserFileLine> rdpList= NewRDPParserFileLine.getRdpList(inFile);
		
		for( NewRDPParserFileLine rdp : rdpList )
		{
			boolean keepGoing = true;
			for( int x=startIndex; keepGoing &&  x<=endIndex; x++)
			{
				NewRDPNode node = rdp.getTaxaMap().get(TAXA_LEVELS[x]);
				
				if( node.getScore() <= RdpReparse.RDP_THRESHOLD )
				{
					keepGoing = false;
					String unclassifiedString = buildUnclassifiedString(rdp, x);
					
					Long aLong = map.get(unclassifiedString);
					
					if( aLong == null)
						aLong = 0l;
					
					aLong++;
					
					map.put(unclassifiedString, aLong);
					
				}
			}
		}
		
		return map;
	}
	
	private static String buildUnclassifiedString(NewRDPParserFileLine rdpLine, int startUnclassifiedLevel)
		throws Exception
	{
		StringBuffer buff = new StringBuffer();
		//phylum__Acidobacteria|class__Acidobacteria_Gp1|order__Unclassified Acidobacteria_Gp1 Class|family__Unclassified Acidobacteria_Gp1 Class|genus__Unclassified Acidobacteria_Gp1 Class
		
		Map<String, NewRDPNode> taxaMap = rdpLine.getTaxaMap();
		
		for( int x=0; x < TAXA_LEVELS.length; x++ )
		{
			buff.append(TAXA_LEVELS[x] + "__");
			
			if( x < startUnclassifiedLevel)
			{
				String taxaName = taxaMap.get(TAXA_LEVELS[x]).getTaxaName();
				buff.append(taxaName);
			}
			else
			{
				buff.append("Unclassified " + taxaMap.get(TAXA_LEVELS[startUnclassifiedLevel]).getTaxaName() + " "+
									TAXA_LEVELS_FIRST_UPPER[startUnclassifiedLevel]);
			}
			
			if( x + 1 < TAXA_LEVELS.length)
				buff.append("|");
		}
		
		
		return buff.toString();
	}
	
	private static String getLastPipeSubstring(String s)
	{
		s = s.substring(s.lastIndexOf("|") + 1, s.length());
		return s;
	}
	
	public static void assertUnclassifiedEquals(File biolockJFile, HashMap<String, Long> map) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(biolockJFile));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			if( sToken.countTokens() != 2)
				throw new Exception("Expecting two tokens " );
			
			String taxa =sToken.nextToken();
			Long count = Long.parseLong(sToken.nextToken());
			
			if( getLastPipeSubstring(taxa).toLowerCase().indexOf("unclassified")!=-1)
			{

				Long anotherCount = map.get(taxa);
				
				if( anotherCount == null)
					throw new Exception("Could not find " + taxa + " " + count);
				
				if( ! count.equals(anotherCount))
					throw new Exception("Taxa mismatch " + taxa +  " " + count + " " + anotherCount);
				
				System.out.println("match " + taxa + " " + count);
			}
			
		}
	}
	
	/*
	 * 
	 * Hard-coded file path for the development cycle here.
	 * Obviously, this main method will not be part of the automated test suite
	 */
	public static void main(String[] args) throws Exception
	{
		File inFile =new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\verifyRDPParser_2019Jul08\\02_RdpClassifier\\output\\ERR1456828_reported.tsv");
		
		HashMap<String, Long> map = getUnclassifiedMap(inFile, "phylum",  "genus");
		
		for(String s : map.keySet())
		{
			if( s.indexOf("Gp1") != -1 )
				System.out.println(s + " " +  map.get(s) );
		}
		
		File matchingFile = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\verifyRDPParser_2019Jul08\\03_RdpParser\\output\\verifyRDPParser_2019Jul08_otuCount_ERR1456828.tsv");
		
		assertUnclassifiedEquals(matchingFile,map);
	}
}
