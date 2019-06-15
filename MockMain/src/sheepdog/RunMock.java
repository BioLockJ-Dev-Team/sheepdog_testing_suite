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
	private static String EXPECTED_OUTCOME_COL = "ExpectedOutcome";
	private static String OUTCOME_SEEN_COL = "Observed";
	private static String PASS_FAIL = "Pass/Fail";
	private static String NOTES_COL = "Notes";
	
	private static HashMap<Integer, String> configs = new HashMap<Integer, String>();
	private static HashMap<Integer, String> out_exp = new HashMap<Integer, String>();
	private static HashMap<Integer, String> notes = new HashMap<Integer, String>();
	
	
	public static void main(String args[]) {
		try {
		readTestList(args[0]);
		
		final BufferedWriter writer = new BufferedWriter( new FileWriter( new File(args[1]) ) );
		writeHeader( writer );
		
		for (Integer id = 1; id < configs.size(); id++ ) { 
				String returned = runMockBljMain(configs.get( id ));
				writeResult( writer, id, returned );
		}
		
		writer.close();
		}catch(Exception e) {
			System.out.println("There was a problem...");
			e.printStackTrace();
		}
	}
	
	private static void readTestList( String filePath ) throws FileNotFoundException, IOException
	{
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

				configs.put( key, row.get( iConfig ) );
				out_exp.put( key, row.get( iExpected ) );
				notes.put( key, row.get( iNotes ) );

				key = key + 1;
			}
		}
		finally
		{
			reader.close();
		}

	}
	
	public static String runMockBljMain (String configF) throws Exception {
		String localPath = Config.replaceEnvVar( "${SHEP}/MockMain/dist/MockMain.jar:${BLJ}/dist/BioLockJ.jar" );
		String localPipes = Config.replaceEnvVar( "${SHEP}/MockMain/pipelines" );
		String cmd = "java -cp " + localPath + " sheepdog.MockMain "
				+ "-b " + localPipes + " -u ~ -c " + Config.replaceEnvVar(configF);
		System.err.println(cmd);
		String result = launchBioLockJ(cmd);
		return result;
	}
	
	public static String launchBioLockJ( final String cmd ) {
		String result = "";
		try {
			final Process p = Runtime.getRuntime().exec( cmd );
			final BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String s = null;
			while( ( s = br.readLine() ) != null )
				if( s.startsWith( MockMain.KEY ) ) {
					result = s.replace( MockMain.KEY, "" ).trim();
					break;
				}
			p.waitFor();
			p.destroy();
		} catch( final Exception ex ) {
			System.err.println( "Problem occurred running command: " + cmd);
		}
		return result;
	}
	
	public static void writeHeader( BufferedWriter writer ) throws IOException{
		ArrayList<String> row = new ArrayList<String>();
		row.add( CONFIG_FILE_COL );
		row.add( EXPECTED_OUTCOME_COL );
		row.add( OUTCOME_SEEN_COL );
		row.add( PASS_FAIL );
		row.add( NOTES_COL );
		
		writer.write( String.join( Constants.TAB_DELIM, row ) + Constants.RETURN );
	}
	
	public static void writeResult( BufferedWriter writer, Integer key, String result ) throws IOException{
		ArrayList<String> row = new ArrayList<String>();
		row.add( configs.get( key ) );
		row.add( out_exp.get( key ) );
		row.add( result );
		row.add( result.equals( out_exp.get( key ) ) ? "PASS" : "FAIL" );
		row.add( notes.get( key ) );
		
		writer.write( String.join( Constants.TAB_DELIM, row ) + Constants.RETURN );
	}
		
	
}
