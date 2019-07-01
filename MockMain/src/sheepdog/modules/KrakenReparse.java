package sheepdog.modules;

import java.io.File;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModuleImpl;
import biolockj.util.ModuleUtil;

public class KrakenReparse extends BioModuleImpl
{
	//todo : Get these from the property file
	//private static final String GENUS_LEVEL = "genus";

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
			Log.info( getClass(), "GOT " + aFile.getAbsolutePath()  + " as biolockJ summary of Kraken output" );
		
		if( biolockJSummaryFiles.size() ==0  )
			throw new Exception("No summary files");

		List<File> krakenOutput = 
				ModuleUtil.getPreviousModule(this).getInputFiles();
		
		for( File aFile : krakenOutput)
			Log.info(getClass(), "GOT " + aFile.getAbsolutePath() + " as Kraken output file");
		
		if( krakenOutput.size() == 0 )
			throw new Exception("No Kraken output");
		
		if( krakenOutput.size() != biolockJSummaryFiles.size())
			throw new Exception("Unequal numbers of files " + biolockJSummaryFiles.size()+ " " + krakenOutput.size());
		
		for(File krakenFile : krakenOutput)
		{
			File biolockJSummary = findMatchingSummaryFile(krakenFile, biolockJSummaryFiles);
			Log.info(getClass(), "Getting ready to parse " + krakenFile.getAbsolutePath());
			//HashMap<String, Long> expectationMap = getCountsForALevel(rdpFile, GENUS_LEVEL);
			//assertEqual(biolockJSummary, expectationMap, GENUS_LEVEL);
		}
		
		Log.info( getClass(),	 "Exiting RdpReparse module");
		
	}
	

	private static File findMatchingSummaryFile( File rdpOutputFile,  List<File> biolockJSummaryFiles) throws Exception
	{
		
		String sampleID = rdpOutputFile.getName();
		sampleID = sampleID.substring(sampleID.lastIndexOf("_") +1 , sampleID.length());
		sampleID = sampleID.replace(".tsv", "");
		
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
	
	/*
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
			System.out.println("COULD NOT FIND  " + innerMap);
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
	*/
	
}
