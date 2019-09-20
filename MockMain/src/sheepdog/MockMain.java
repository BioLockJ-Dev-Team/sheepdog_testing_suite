package sheepdog;

import biolockj.exception.FatalExceptionHandler;
import biolockj.util.BioLockJUtil;

public class MockMain extends biolockj.BioLockJ
{
	
	public static void main(String args[]) {
		try {
			initBioLockJ( args );
			runPipeline();
		} catch( final Exception ex ) {
			FatalExceptionHandler.logFatalError( args, ex );
		} finally {
			if( !BioLockJUtil.isDirectMode() ) pipelineShutDown();
		}
	}
	
}
