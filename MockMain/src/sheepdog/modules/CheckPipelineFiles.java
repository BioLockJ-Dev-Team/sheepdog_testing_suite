package sheepdog.modules;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import biolockj.BioLockJ;
import biolockj.Config;
import biolockj.Constants;
import biolockj.exception.ConfigPathException;
import biolockj.module.BioModuleImpl;

public class CheckPipelineFiles extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		checkFiles( false );
	}

	@Override
	public void executeTask() throws Exception {
		checkFiles( true );
	}
	
	/**
	 * Copy any files listed in checkPipelineFiles.pipelineFiles.
	 * This means that when the validation utility runs, it will validate those files.
	 * This is intended for files at the pipeline top level that would not be validated through any module.
	 */
	private void checkFiles( boolean makeCopy ) throws ConfigPathException, IOException {
		Set<String> files = Config.getSet( this, CHECK_FILES_PROP );
		for( String f: files ) {
			File file = new File( BioLockJ.getPipelineDir(), f );
			if( makeCopy ) {
				if( file.exists() ) {
					FileUtils.copyFile( file, new File( getOutputDir(), file.getName() ) );
				} else {
					throw new ConfigPathException( file );
				}
			}
		}
	}
	
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}

	@Override
	public String getDockerImageOwner() {
		return Constants.MAIN_DOCKER_OWNER;
	}
	
	private static final String CHECK_FILES_PROP = "checkPipelineFiles.pipelineFiles";

}
