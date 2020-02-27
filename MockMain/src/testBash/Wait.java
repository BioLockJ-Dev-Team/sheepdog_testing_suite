package testBash;

import biolockj.Config;
import biolockj.Constants;
import biolockj.module.BioModuleImpl;

public class Wait extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeTask() throws Exception {
		// TODO Auto-generated method stub
		int milliseconds = Config.getNonNegativeInteger( this, "wait.seconds" ) * 1000;
		Thread.sleep( milliseconds );
	}
	
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}

}
