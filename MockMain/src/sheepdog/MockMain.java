package sheepdog;

import biolockj.Config;
import biolockj.Constants;
import biolockj.exception.ConfigFormatException;
import biolockj.exception.ConfigPathException;
import biolockj.exception.FatalExceptionHandler;
import biolockj.module.BioModule;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;

public class MockMain extends biolockj.BioLockJ
{
	private static String COMPLETE = "BioLockJ_Complete";
	protected static String RESULT_KEY = "HERE_IS_THE_RESULT__";
	protected static String PIPELINE_KEY = "HERE_IS_THE_PIPELINE__";
	protected static String VALIDATION_ENABLED = "VALIDATION_IS_ENABLED";
	
	public static void main(String args[]) {
		System.err.println( "Testing BioLockj..." + args[5] + " ... " + Constants.APP_START_TIME );
		String result = "default String";
		try {
			initBioLockJ( args );
			System.out.println( PIPELINE_KEY + Config.getPipelineDir().getAbsolutePath() );
			checkValidation();
			runPipeline();
			result = COMPLETE;
		} catch( final Exception ex ) {
			result = ex.getClass().getSimpleName();
			FatalExceptionHandler.logFatalError( args, ex );
		} finally {
			System.out.println( RESULT_KEY + result );
			if( !BioLockJUtil.isDirectMode() ) pipelineShutDown();
		}
	}
	
	public static void checkValidation() throws ConfigFormatException, ConfigPathException {
		BioModule module = new BioModuleImpl() {
			@Override
			public void executeTask() throws Exception {}
			@Override
			public void checkDependencies() throws Exception {}
		};
		
		if (Config.getBoolean( module, "validation.stopPipeline" )
						&& !Config.getBoolean( module, "validation.disableValidation" )
						&& Config.getString( module, "validation.expectationFile" ) != null ) {
			System.out.println(VALIDATION_ENABLED);
		}
		System.out.println(VALIDATION_ENABLED + "-->NOPE");
	}
	
}
