package sheepdog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import biolockj.Config;
import biolockj.Constants;
import biolockj.Processor;
import biolockj.Properties;
import biolockj.util.BioLockJUtil;
import biolockj.util.SummaryUtil;
import biolockj.exception.FatalExceptionHandler;

public class TestBioLockJ
{
	private static final String COMPLETE = "BioLockJ_Complete";
	private static final String CONFIG_FILE_COL = "ConfigFile";
	private static final String JAVA_ARGS_COL = "java_BioLockJ_args";
	private static final String BASH_ARGS_COL = "bash_biolockj_args";
	private static final String PIPELINE_COL = "PipelineDirectory";
	private static final String VAL_ENABLED_COL = "ValidationEnabled";
	private static final String EXP_COMPLETE_MODS_COL = "NumberShouldComplete";
	private static final String NUM_COMPLETE_MODS_COL = "NumberCompletedModules";
	private static final String EXPECTED_OUTCOME_COL = "ExpectedOutcome";
	private static final String OUTCOME_SEEN_COL = "Observed";
	private static final String PASS_FAIL = "Pass/Fail";
	private static final String NOTES_COL = "Notes";
	private static final String BLJ_JAR="${BLJ}/dist/BioLockJ.jar";
	private static final String MOCK_JAR="${SHEP}/MockMain/dist/TestBioLockJ.jar";
	private static final String TMP_PROJ="${SHEP}/MockMain/pipelines";
	private static final String NOT_IN_GIT="NOT_IN_GIT";
	
	private static class TestInfoRow {
		File config;
		String java_args;
		String bash_args;
		String pipeline;
		int expCompleteModules = -1;
		int completedModules;
		boolean validationEnabled=false;
		String out_exp;
		String result;
		boolean passes;
		String notes;
		
		String getField(String colName){
			switch (colName) {
				case CONFIG_FILE_COL:
					return( config.getAbsolutePath() );
				case JAVA_ARGS_COL:
					return( java_args );
				case BASH_ARGS_COL:
					return( bash_args );
				case PIPELINE_COL:
					return( pipeline );
				case VAL_ENABLED_COL:
					return( validationEnabled ? "YES" : "NO" );
				case EXP_COMPLETE_MODS_COL:
					if (expCompleteModules == -1) return("NA");
					return( Integer.toString( expCompleteModules ) );
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
			CONFIG_FILE_COL, JAVA_ARGS_COL, BASH_ARGS_COL, PIPELINE_COL, VAL_ENABLED_COL, 
			EXP_COMPLETE_MODS_COL, NUM_COMPLETE_MODS_COL,
			EXPECTED_OUTCOME_COL, OUTCOME_SEEN_COL, PASS_FAIL, NOTES_COL) );
	
	private static ArrayList<TestInfoRow> tests = new ArrayList<TestInfoRow>();
	
	public static void main( String args[] ){
		long startTime = System.currentTimeMillis();
		int surprises = 0;
		int testsRun = 0;
		ArrayList<String> summary = new ArrayList<String>();
		String outFileName = createOutFileName( args[ 0 ] );
		try{
			readTestList( args[ 0 ] );
			
			System.err.println( "Test results will be written to: " + outFileName );
			final BufferedWriter writer = new BufferedWriter( new FileWriter( new File( outFileName ) ) );
			writeComments(writer);
			writer.write( String.join( Constants.TAB_DELIM, outputHeader ) + System.lineSeparator() );
			
			if ( shouldClearPipelines(args) ) clearPipelines();
			
			for( TestInfoRow test : tests ){
				System.err.println("=====");
				runMockBljMain( test );
				testsRun ++;
				System.err.println( "This pipeline completed " + getCompletedModuleCount( test ) + " modules.");
				if( test.out_exp.equals( test.result ) && (test.expCompleteModules==test.completedModules || test.expCompleteModules== -1) ){
					test.passes = true;
				}else {
					test.passes = false;
					surprises++;
					System.err.println( "SURPRISE! This didn't give me what I expected: " + test.config.getName() );
				}
				
				writer.write( test.toString() );
			}

			writer.close();
			
			summary.add( "Number of tests that failed:   " + surprises );
			summary.add( "Number of tests that were run: " + testsRun );
			summary.add( "Total test runtime: " + SummaryUtil.getRunTime( System.currentTimeMillis() - startTime ) );
			
			System.err.println( System.lineSeparator() + "DONE!" + System.lineSeparator() );

			addSummaryAtTop(outFileName, summary);
			
			if ( args.length > 1 ) {
				appendCollectionResults(outFileName, args[ 1 ]);
			}
		}
		catch( Exception e ){
			System.err.println( "There was a problem..." );
			e.printStackTrace();
			System.exit( 1 );
		}
		
		System.exit( 0 );
	}
	
	protected static String createOutFileName(String inFile) {
		File inF = new File(inFile);
		String name = inF.getName().substring( 0, inF.getName().lastIndexOf( "." ) ) + "_results_" + NOT_IN_GIT + ".txt" ;
		return (new File(inF.getParentFile(), name)).getAbsolutePath();
	}
	
	protected static void readTestList( String filePath ) throws Exception
	{
		System.err.println("Reading test list from: " + filePath);
		File inFile = new File( filePath );
		int iConfig = 0;
		int iJavaArgs = 0;
		int iBashArgs = 0;
		int iCompModsExp = 0;
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
					iJavaArgs = row.indexOf( JAVA_ARGS_COL );
					iBashArgs = row.indexOf( BASH_ARGS_COL );
					iCompModsExp = row.indexOf( EXP_COMPLETE_MODS_COL );
					iExpected = row.indexOf( EXPECTED_OUTCOME_COL );
					iNotes = row.indexOf( NOTES_COL );
				}else {
					TestInfoRow oneTest = new TestInfoRow();
					oneTest.config = new File( replaceEnvVar( row.get( iConfig ) ) );
					if (iJavaArgs > 0 ) oneTest.java_args = row.get( iJavaArgs ); else oneTest.java_args = "";
					if (iBashArgs > 0 ) oneTest.bash_args = row.get( iBashArgs ); else oneTest.bash_args = "";
					if (iCompModsExp > 0 && !row.get( iCompModsExp ).isEmpty() ) oneTest.expCompleteModules = Integer.parseInt( row.get( iCompModsExp ) ) ;
					oneTest.out_exp = row.get( iExpected );
					if (iNotes > 0 ) oneTest.notes = row.get( iNotes ); else oneTest.notes = "";
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
	
	public static String extractInputDir(File config) throws Exception
	{
		System.err.println("Extracting input.dirPaths value from: " + config.getName() );
		Properties props = Properties.loadProperties( config );
		String inputDir = replaceEnvVar((String) props.get( "input.dirPaths" ));
		File input = new File(inputDir);
		if (!input.exists() || !input.isDirectory()) {
			throw new Exception("To run in docker, input.dirPaths should give a single valid directory."
					+ "\nCould not find directory: " + input );
		}
		return inputDir;
	}
	
	protected static void runMockBljMain (TestInfoRow test) throws Exception {
		String cmd;
		if ( test.bash_args.isEmpty() && ! test.java_args.isEmpty() ) {
			cmd = generateJavaCmd(test);
		}else if ( ! test.bash_args.isEmpty() && ! test.java_args.isEmpty() ) {
			throw new Exception("Can't use both " + BASH_ARGS_COL + " and " + JAVA_ARGS_COL + " in the same test." );
		}else {
			cmd = "biolockj -f --blj_proj " + replaceEnvVar(TMP_PROJ) + " " 
							+ replaceEnvVar(test.bash_args) + " " + test.config;
		}
		System.err.println("Command for subprocess: " + cmd);
		
		String result = "";
		String pipeline = "NA";
		try {
			final Process p = Runtime.getRuntime().exec( cmd ); 
			final BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String s = null;
			while( ( s = br.readLine() ) != null )
			{
				if( s.startsWith( SummaryUtil.PIPELINE_STATUS ) && s.endsWith( Constants.SCRIPT_SUCCESS.toUpperCase() )) {
					result = COMPLETE;
				}
				if( s.startsWith( FatalExceptionHandler.ERROR_TYPE ) ) {
					result = s.replace( FatalExceptionHandler.ERROR_TYPE, "" ).trim();
				}
				if( s.startsWith( Constants.PIPELINE_LOCATION_KEY ) ){
					pipeline = s.replace( Constants.PIPELINE_LOCATION_KEY, "" ).trim();
					pipeline = pipeline.replace( "/mnt/efs/pipelines", replaceEnvVar(TMP_PROJ) ); //for docker case
				}
				if( s.contentEquals( Constants.VALIDATION_ENABLED )) {
					test.validationEnabled=true;
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
	
	private static String generateJavaCmd(TestInfoRow test) {
		String cmd;
		cmd = "java -cp " + replaceEnvVar(MOCK_JAR) + ":" + replaceEnvVar(BLJ_JAR) 
		+ " biolockj.BioLockJ " + "-b " + replaceEnvVar(TMP_PROJ) 
		+ " -u ~ -c " + test.config.getAbsolutePath()+ " " + test.java_args ;
		System.err.println(cmd);
		return( cmd );
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
		writer.write( "# BioLockJ jar file: " + replaceEnvVar(BLJ_JAR) + System.lineSeparator() );
		writer.write( "# BioLockJ version: " + getJarVersion() + System.lineSeparator() );
		writer.write( "# SHEP_DATA: " + (new File(replaceEnvVar("${SHEP_DATA}"))).getName() + System.lineSeparator());
	}
	
	protected static String getJarVersion() {
		String version = "";
		String cmd = "java -cp " + replaceEnvVar(BLJ_JAR) + " biolockj.BioLockJ --version";
		try {
			final Process p = Runtime.getRuntime().exec( cmd );
			final BufferedReader br = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
			String s;
			while( ( s = br.readLine() ) != null )
			{
				version = String.valueOf( s );
			}
			p.waitFor();
			p.destroy();
		}catch(Exception ex) {
			System.err.println("There was an error while getting the jar file version.");
			System.err.println(cmd);
		}
		if (version == null || version.isEmpty()) {
			System.err.println(cmd);
		}
		return( version );
	}
	
	protected static boolean shouldClearPipelines(String[] args) {
		boolean clear = true;
		if ( args.length > 1 ) {
			clear = ! (new File(args[1])).exists();
		}
		return clear;
	}
	
	protected static void clearPipelines() throws IOException {
		String prevPipesDirName = "previousPipelineSet";
		File pipesDir = new File(replaceEnvVar(TMP_PROJ));
		File prevPipesDir = new File(pipesDir, prevPipesDirName);
		if (prevPipesDir.exists() && prevPipesDir.isDirectory() ) FileUtils.deleteDirectory(prevPipesDir);
		prevPipesDir.mkdir();
		File[] oldPipelines = pipesDir.listFiles();
		int count = 0;
		for (File oldFile : oldPipelines) {
			if (oldFile.isDirectory() && ! oldFile.getName().equals( prevPipesDirName )) {
				oldFile.renameTo( new File(prevPipesDir, oldFile.getName()) );
				count++;
			}else if (oldFile.getName().startsWith( "README" )) {
			}else {
				oldFile.delete();
			}
		}
		System.err.println("Moved " + count + " directories from $SHEP/pipelines into previousPipelineSet; these will be deleted the next time this program is run.");
	}
		
	/**
	 * Replace any variables surrounded with "${" and "}" with the variables value from the environment.
	 * @param string
	 * @param var
	 * @return
	 */
	private static String replaceEnvVar(final String string) {
		String result = string;
		while(result.contains( "${" ) && result.contains( "}" ) && result.indexOf( "}" ) > result.indexOf( "$}" )) {
			String var = result.substring( result.indexOf( "${" ) + 2, result.indexOf( "}" ) );
			String val = System.getenv( var );
			String newString = result.substring( 0,  result.indexOf( "${" )) + val + result.substring( result.indexOf( "}" ) + 1 );
			result = newString;
		}
		//System.out.println("Replaced env var to conver [" + string + "] into: " + result);
		return result;
	}

	protected static void addSummaryAtTop(String outFileName, Collection<String> summary) throws IOException {
		String tmpFile = outFileName + "~";
		final BufferedReader reader = BioLockJUtil.getFileReader( new File(outFileName) );
		final BufferedWriter writer = new BufferedWriter( new FileWriter( new File( tmpFile ) ) );
		for (String line : summary) {
			System.err.println( line );
			writer.write( "# " + line + System.lineSeparator() );
		}
		for( String line = reader.readLine(); line != null; line = reader.readLine() )
		{
			writer.write( line + System.lineSeparator() );
		}
		writer.close();
		Files.delete( Paths.get( outFileName ) );
		Files.move( Paths.get( tmpFile ), Paths.get(outFileName) );
	}
	
	/*
	 * If this tests set is being run as part of a larger collection, 
	 * then the results will additionally be appended to the table for the collection.
	 */
	protected static void appendCollectionResults(String outFileName, String collectionOutTable) throws IOException {
		if ( collectionOutTable != null ) {
			final BufferedReader reader = BioLockJUtil.getFileReader( new File(outFileName) );
			File collectionFile = new File(collectionOutTable);
			final BufferedWriter writer = new BufferedWriter( new FileWriter( collectionFile, true ) );
			
			if ( collectionFile.length() == 0 ) { //collectionFile.createNewFile()
				writeComments(writer);
				writer.write( String.join( Constants.TAB_DELIM, outputHeader ) + System.lineSeparator() );
			}
			
			for( String line = reader.readLine(); line != null; line = reader.readLine() )
			{
				if ( !line.startsWith( "#" ) && !line.startsWith( outputHeader.get( 0 ) ) ) 
					writer.write( line + System.lineSeparator() );
			}
			writer.close();
		}
	}
}
