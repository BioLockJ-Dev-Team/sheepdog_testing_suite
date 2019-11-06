package sheepdog.modules;

import java.io.File;
import org.apache.commons.io.FileUtils;
import biolockj.Config;
import biolockj.module.BioModuleImpl;
import biolockj.util.MasterConfigUtil;

public class CopyMasterProps extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// no dependencies
	}

	@Override
	public void executeTask() throws Exception {
		File config = MasterConfigUtil.getExistingMasterConfig( Config.getPipelineDir() );
		File dest = getOutputDir();
		FileUtils.copyFileToDirectory( config, dest );
	}

}
