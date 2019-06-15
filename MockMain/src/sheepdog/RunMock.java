package sheepdog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import biolockj.Config;
import biolockj.Constants;
import biolockj.util.BioLockJUtil;

public class RunMock
{
	private static String CONFIG_FILE_COL = "ConfigFile";
	private static String PIPELINE_COL = "PipelineDirectory";
	private static String NUM_COMPLETE_MODS_COL = "NumberCompletedModules";
	private static String EXPECTED_OUTCOME_COL = "ExpectedOutcome";
	private static String OUTCOME_SEEN_COL = "Observed";
	private static String PASS_FAIL = "Pass/Fail";
	private static String NOTES_COL = "Notes";
	private static String HACKERS_PIPELINE_KEY = "Set Config property [ internal.pipelineDir ] = ";
	
	private static HashMap<Integer, File> configs = new HashMap<Integer, File>();
	private static HashMap<Integer, String> out_exp = new HashMap<Integer, String>();
	private static HashMap<Integer, String> notes = new HashMap<Integer, String>();
	private static HashMap<Integer, String> results = new HashMap<Integer, String>();
	private static HashMap<Integer, String> pipelines = new HashMap<Integer, String>();
	
	
	public static void main( String args[] ){
		int surprises = 0;
		try{
			readTestList( args[ 0 ] );
			
			System.err.println( "Test results will be written to: " + args[ 1 ] );
			final BufferedWriter writer = new BufferedWriter( new FileWriter( new File( args[ 1 ] ) ) );
			writeHeader( writer );
			
			for( Integer id = 1; id < configs.size(); id++ ){
				runMockBljMain( id );
				if( !results.get( id ).equals( out_exp.get( id ) ) ){
					surprises++;
					System.err.println( "HEY! This didn't give me what I expected: " + configs.get( id ).getName() );
				}
				writeResult( writer, id );
			}
			
			writer.close();
		}
		catch( Exception e ){
			System.err.println( "There was a problem..." );
			e.printStackTrace();
			System.exit( 1 );
		}
		System.err.println( "DONE!" );
		System.err.println( "We had: " + surprises + " surprises." );
		System.err.println( "Test results written to: " + args[ 1 ] );
		System.exit( 0 );
	}
	
	protected static void readTestList( String filePath ) throws FileNotFoundException, IOException
	{
		System.err.println("Reading test list from: " + filePath);
		File inFile = new File( filePath );
		int iConfig = 0;
		int iExpected = 0;
		int iNotes = 0;
		Integer key = new Integer( 0 );
		BufferedReader reader = BioLockJUtil.getFileReader( inFile );

		try
		{
			for( String line = reader.readLine(); line != null; line = reader.readLine() )
			{
				final String[] cells = line.split( Constants.TAB_DELIM, -1 );
				final ArrayList<String> row = new ArrayList<String>( Arrays.asList( cells ) );
				
				if( key.equals( 0 ) )
				{
					iConfig = row.indexOf( CONFIG_FILE_COL );
					iExpected = row.indexOf( EXPECTED_OUTCOME_COL );
					iNotes = row.indexOf( NOTES_COL );
				}

				configs.put( key, new File(Config.replaceEnvVar(row.get( iConfig ))) );
				out_exp.put( key, row.get( iExpected ) );
				notes.put( key, row.get( iNotes ) );

				key = key + 1;
			}
		}
		finally
		{
			reader.close();
		}
		System.err.println("Read in list of " + (configs.size() - 1)  + " tests");
	}
	
	protected static void runMockBljMain (Integer id) throws Exception {
		File configF = configs.get( id );
		String localPath = Config.replaceEnvVar( "${SHEP}/MockMain/dist/MockMain.jar:${BLJ}/dist/BioLockJ.jar" );
		String localPipes = Config.replaceEnvVar( "${SHEP}/MockMain/pipelines" );
		String cmd = "java -cp " + localPath + " sheepdog.MockMain "
				+ "-b " + localPipes + " -u ~ -c " + configF.getAbsolutePath();
		System.err.println(cmd);
		String result = "";
		String pipeline = "NA";
		try {
			final Process p = Runtime.getRuntime().exec( cmd );
			final BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String s = null;
			while( ( s = br.readLine() ) != null )
			{
				if( s.startsWith( MockMain.RESULT_KEY ) )
				{
					result = s.replace( MockMain.RESULT_KEY, "" ).trim();
				}
				if( s.contains( HACKERS_PIPELINE_KEY ) )
				{
					pipeline = s.replaceFirst( ".*" + HACKERS_PIPELINE_KEY, "" ).trim();
					System.err.println("Created pipeline dir: " + pipeline);
				}
				if( s.startsWith( MockMain.PIPELINE_KEY ) )
				{
					pipeline = s.replace( MockMain.PIPELINE_KEY, "" ).trim();
				}
			}
			p.waitFor();
			p.destroy();
		} catch( final Exception ex ) {
			System.err.println( "Problem occurred running command: " + cmd);
			ex.printStackTrace();
		}
		results.put(id, result);
		pipelines.put(id, pipeline);
		System.err.println( "Finished running: " + configF.getName() );
	}
	
	protected static void writeHeader( BufferedWriter writer ) throws IOException{
		ArrayList<String> row = new ArrayList<String>();
		row.add( CONFIG_FILE_COL );
		row.add( PIPELINE_COL );
		row.add( NUM_COMPLETE_MODS_COL );
		row.add( EXPECTED_OUTCOME_COL );
		row.add( OUTCOME_SEEN_COL );
		row.add( PASS_FAIL );
		row.add( NOTES_COL );
		
		writer.write( String.join( Constants.TAB_DELIM, row ) + Constants.RETURN );
	}
	
	protected static void writeResult( BufferedWriter writer, Integer key ) throws IOException{
		ArrayList<String> row = new ArrayList<String>();
		row.add( configs.get( key ).getAbsolutePath() );
		row.add( pipelines.get( key ) );
		row.add( getCompletedModuleCount( pipelines.get( key ) ) );
		row.add( out_exp.get( key ) );
		row.add( results.get( key ) );
		row.add( results.get( key ).equals( out_exp.get( key ) ) ? "PASS" : "FAIL" );
		row.add( notes.get( key ) );
		writer.write( String.join( Constants.TAB_DELIM, row ) + Constants.RETURN );
		System.err.println("Added " + key + "th row to output.");
	}
	
	private static String getCompletedModuleCount( String pipeline ) {
		File top = new File(pipeline);
		if (top.exists() && top.isDirectory()) {
			File[] subdirs = top.listFiles(File::isDirectory);
			int completed = 0;
			for (File module : subdirs) {
				for (File f : module.listFiles()) {
					if (f.getName().equals( "biolockjComplete" )) {
						completed++;
						break;
					}
				}
			}
			return(Integer.toString( completed ));
		}else {
			return("NA");
		}
	}
		
	
}
