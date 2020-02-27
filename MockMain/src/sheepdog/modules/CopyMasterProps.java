package sheepdog.modules;

import java.io.File;
import org.apache.commons.io.FileUtils;
import biolockj.Config;
import biolockj.Constants;
import biolockj.api.ApiModule;
import biolockj.module.BioModuleImpl;
import biolockj.util.MasterConfigUtil;

public class CopyMasterProps extends BioModuleImpl implements ApiModule{

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

	@Override
	public String getDescription() {
		return "Copy the master properties file";
	}

	@Override
	public String getCitationString() {
		return "Ivory Blakley";
	}
	
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}

}
