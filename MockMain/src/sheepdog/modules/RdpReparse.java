package sheepdog.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.ModuleUtil;
import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

public class RdpReparse extends BioModuleImpl
{
	//todo : Get these from the property file
	private static final int RDP_THRESHOLD = 80;
	private static final String GENUS_LEVEL = "genus";

	@Override
	public void checkDependencies() throws Exception
	{
		//Log.info( getClass(), "IN stub for checkDependencies()");
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for " + getClass().getName());
	
		List<File> biolockJSummaryFiles= getInputFiles();
		
		for( File aFile : biolockJSummaryFiles)
			Log.info( getClass(), "GOT " + aFile.getAbsolutePath()  + " as biolockJ summary of RDP output" );
		
		if( biolockJSummaryFiles.size() ==0  )
			throw new Exception("No summary files");

		List<File> rdpOutput = 
				ModuleUtil.getPreviousModule(this).getInputFiles();
		
		for( File aFile : rdpOutput)
			Log.info(getClass(), "GOT " + aFile.getAbsolutePath() + " as RDP output file");
		
		if( rdpOutput.size() == 0 )
			throw new Exception("No RDP output");
		
		if( rdpOutput.size() != biolockJSummaryFiles.size())
			throw new Exception("Unequal numbers of files " + biolockJSummaryFiles.size()+ " " + rdpOutput.size());
		
		for(File rdpFile : rdpOutput)
		{
			File biolockJSummary = findMatchingSummaryFile(rdpFile, biolockJSummaryFiles);
			Log.info(getClass(), "Getting ready to parse " + rdpFile.getAbsolutePath());
			HashMap<String, Long> expectationMap = getCountsForALevel(rdpFile, GENUS_LEVEL);
			assertEqual(biolockJSummary, expectationMap, GENUS_LEVEL);
		}
		
		Log.info( getClass(),	 "Exiting RdpReparse module");
		
	}
	
	private static void assertEqual( File inFile, HashMap<String,Long> innerMap, String level ) throws Exception
	{
		System.out.println("check " + inFile.getAbsolutePath());
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		int checked =0;
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine() )
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			if( sToken.countTokens() != 2)
				throw new Exception("Expecting two tokens");
			
			String name = sToken.nextToken();
			
			if( name.toLowerCase().indexOf("unclassified") == -1)
			{
				name = name.substring(name.indexOf(level + "__"), name.length());
				name = name.replace(level + "__", "");
				
				Long parsedVal = innerMap.get(name);
				
				if( parsedVal == null)
					throw new Exception("Could not find " + name);
				
				Long thisVal = Long.parseLong(sToken.nextToken());
				
				if( ! thisVal.equals(parsedVal))
					throw new Exception("Mismatched for " + name + " " + parsedVal + " " + thisVal + " " + inFile.getAbsolutePath());
				
				checked++;
				
				innerMap.remove(name);
			}
		}
		
		System.out.println("Ok checked " + checked);
		
		if( innerMap.size() != 0)
			throw new Exception("COULD NOT FIND  " + innerMap);
	}
	
	private static File findMatchingSummaryFile( File rdpOutputFile,  List<File> biolockJSummaryFiles) throws Exception
	{
		String sampleID = rdpOutputFile.getName().replace("_reported.tsv", "");
		
		File returnFile = null;
		
		for(File f : biolockJSummaryFiles)
		{
			if( f.getName().indexOf(sampleID) != - 1 )
			{
				if( returnFile != null)
					throw new Exception("Duplicate for " + sampleID);
				
				returnFile = f;
			}
		}
		
		if( returnFile == null)
			throw new Exception("Could not find " + sampleID );
		
		return returnFile;
		
	}
	
	private static HashMap<String, Long> getCountsForALevel( File aFile , String level) throws Exception
	{
		HashMap<String, Long> returnMap = new HashMap<>();
		
		HashMap<String, NewRDPParserFileLine> map = NewRDPParserFileLine.getAsMapFromSingleThread(aFile);
		
		for(String s : map.keySet())
		{
			NewRDPParserFileLine aLine = map.get(s);
			
			NewRDPNode aNode = aLine.getTaxaMap().get(level);
			
			if( aNode != null && aNode.getScore() >= RDP_THRESHOLD )
			{
				String taxaName = aNode.getTaxaName();
				
				Long aLong = returnMap.get(taxaName);
				
				if( aLong == null)
					aLong = 0l;
				
				aLong = aLong + 1;
				
				returnMap.put(taxaName, aLong);
			
			}
			
		}
		
		return returnMap;
		
	}
	
}
