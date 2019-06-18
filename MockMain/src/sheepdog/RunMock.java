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
import org.apache.commons.io.FileUtils;
import biolockj.Config;
import biolockj.Constants;
import biolockj.util.BioLockJUtil;
import biolockj.util.SummaryUtil;

public class RunMock
{
	private static final String CONFIG_FILE_COL = "ConfigFile";
	private static final String PIPELINE_COL = "PipelineDirectory";
	private static final String NUM_COMPLETE_MODS_COL = "NumberCompletedModules";
	private static final String EXPECTED_OUTCOME_COL = "ExpectedOutcome";
	private static final String OUTCOME_SEEN_COL = "Observed";
	private static final String PASS_FAIL = "Pass/Fail";
	private static final String NOTES_COL = "Notes";
	private static final String HACKERS_PIPELINE_KEY = "Set Config property [ internal.pipelineDir ] = ";
	private static final String BLJ_JAR="${BLJ}/dist/BioLockJ.jar";
	private static final String MOCK_JAR="${SHEP}/MockMain/dist/MockMain.jar";
	private static final String TMP_PROJ="${SHEP}/MockMain/pipelines";
	private static final String NOT_IN_GIT="NOT_IN_GIT";
	
	private static class TestInfoRow {
		File config;
		String pipeline;
		int completedModules;
		String out_exp;
		boolean passes;
		String result;
		String notes;
		
		String getField(String colName){
			switch (colName) {
				case CONFIG_FILE_COL:
					return( config.getAbsolutePath() );
				case PIPELINE_COL:
					return( pipeline );
				case NUM_COMPLETE_MODS_COL:
					return( Integer.toString(completedModules) );
				case EXPECTED_OUTCOME_COL:
					return( out_exp );
				case OUTCOME_SEEN_COL:
					return( result );
				case PASS_FAIL:
					return( passes ? "PASS" : "FAIL" );
				case NOTES_COL:
					return( notes );
			}
			return null;
		}
		
		public String toString(){
			ArrayList<String> row = new ArrayList<String>();
			for (String s: outputHeader) {
				row.add( getField(s) );
			}
			return( String.join( Constants.TAB_DELIM, row ) + System.lineSeparator() );
		}
	}

	protected static ArrayList<String> outputHeader = new ArrayList<String>( Arrays.asList(
			CONFIG_FILE_COL, PIPELINE_COL, NUM_COMPLETE_MODS_COL,
			EXPECTED_OUTCOME_COL, OUTCOME_SEEN_COL, PASS_FAIL, NOTES_COL) );
	
	private static ArrayList<TestInfoRow> tests = new ArrayList<TestInfoRow>();
	
	public static void main( String args[] ){
		long startTime = System.currentTimeMillis();
		int surprises = 0;
		String outFileName = args.length > 1 ? args[ 1 ] : createOutFileName( args[ 0 ] );
		try{
			readTestList( args[ 0 ] );
			
			System.err.println( "Test results will be written to: " + outFileName );
			final BufferedWriter writer = new BufferedWriter( new FileWriter( new File( outFileName ) ) );
			writeComments(writer);
			writer.write( String.join( Constants.TAB_DELIM, outputHeader ) + System.lineSeparator() );
			
			clearPipelines();
			
			for( TestInfoRow test : tests ){
				System.err.println("=====");
				runMockBljMain( test );
				System.err.println( "This pipeline completed " + getCompletedModuleCount( test ) + " modules.");
				if( test.out_exp.equals( test.result ) ){
					test.passes = true;
				}else {
					test.passes = false;
					surprises++;
					System.err.println( "SURPRISE! This didn't give me what I expected: " + test.config.getName() );
				}
				
				writer.write( test.toString() );
			}
			
			writer.close();
		}
		catch( Exception e ){
			System.err.println( "There was a problem..." );
			e.printStackTrace();
			System.exit( 1 );
		}
		System.err.println( System.lineSeparator() + "DONE!" + System.lineSeparator() );
		System.err.println( "We had: " + surprises + " surprises." );
		System.err.println( "Total test runtime: " + SummaryUtil.getRunTime( System.currentTimeMillis() - startTime ) );
		System.exit( 0 );
	}
	
	protected static String createOutFileName(String inFile) {
		File inF = new File(inFile);
		String name = inF.getName().substring( 0, inF.getName().lastIndexOf( "." ) ) + "_results_" + NOT_IN_GIT + ".txt" ;
		return (new File(inF.getParentFile(), name)).getAbsolutePath();
	}
	
	protected static void readTestList( String filePath ) throws FileNotFoundException, IOException
	{
		System.err.println("Reading test list from: " + filePath);
		File inFile = new File( filePath );
		int iConfig = 0;
		int iExpected = 0;
		int iNotes = 0;
		BufferedReader reader = BioLockJUtil.getFileReader( inFile );

		try
		{
			for( String line = reader.readLine(); line != null; line = reader.readLine() )
			{
				final String[] cells = line.split( Constants.TAB_DELIM, -1 );
				final ArrayList<String> row = new ArrayList<String>( Arrays.asList( cells ) );
				
				if( iNotes==0 ){
					iConfig = row.indexOf( CONFIG_FILE_COL );
					iExpected = row.indexOf( EXPECTED_OUTCOME_COL );
					iNotes = row.indexOf( NOTES_COL );
				}else {
					TestInfoRow oneTest = new TestInfoRow();
					oneTest.config = new File( Config.replaceEnvVar( row.get( iConfig ) ) );
					oneTest.out_exp = row.get( iExpected );
					oneTest.notes = row.get( iNotes );
					tests.add( oneTest );
				}
			}
		}
		finally
		{
			reader.close();
		}
		System.err.println("Read in list of " + (tests.size())  + " tests");
	}
	
	protected static void runMockBljMain (TestInfoRow test) throws Exception {
		String cmd = "java -cp " + Config.replaceEnvVar(MOCK_JAR) + ":" + Config.replaceEnvVar(BLJ_JAR) 
		+ " sheepdog.MockMain " + "-b " + Config.replaceEnvVar(TMP_PROJ) 
		+ " -u ~ -c " + test.config.getAbsolutePath();
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
					int end = s.indexOf( HACKERS_PIPELINE_KEY ) + HACKERS_PIPELINE_KEY.length();
					pipeline = s.substring( end + 1 );
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
		test.result = result ;
		test.pipeline = pipeline ;
	}
	
	protected static void writeHeader( BufferedWriter writer ) throws IOException{
		
	}
	
	protected static void writeResult( BufferedWriter writer, Integer key ) throws IOException{
		
	}
	
	private static String getCompletedModuleCount( TestInfoRow test ) {
		File top = new File(test.pipeline);
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
			test.completedModules = completed;
			return(Integer.toString( completed ));
		}else {
			return( "NA" );
		}
	}
	
	protected static void writeComments(BufferedWriter writer) throws IOException {
		writer.write( "# BioLockJ jar file: " + Config.replaceEnvVar(BLJ_JAR) + System.lineSeparator() );
		writer.write( "# SHEP_DATA: " + (new File(Config.replaceEnvVar("${SHEP_DATA}"))).getName() + System.lineSeparator());
	}
	
	protected static void clearPipelines() throws IOException {
		File pipesDir = new File(Config.replaceEnvVar(TMP_PROJ));
		File[] oldPipelines = pipesDir.listFiles(File::isDirectory);
		int count = 0;
		for (File oldDir : oldPipelines) {
			FileUtils.deleteDirectory(oldDir);
			count++;
		}
		System.err.println("Deleted " + count + " directories from $SHEP/pipelines.");
	}
		
	
}
