package sheepdog.modules.util;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;


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
	/*
	 * the key will be a full string such as 
	 * phylum__Acidobacteria|class__Acidobacteria_Gp1|order__Unclassified Acidobacteria_Gp1 Class|family__Unclassified Acidobacteria_Gp1 Class|genus__Unclassified Acidobacteria_Gp1 Class
	 * 
	 * 
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
				
			}
		}
		
		return map;
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
			System.out.println(s + " " +  map.get(s) + "\n");
		}
	}
}
