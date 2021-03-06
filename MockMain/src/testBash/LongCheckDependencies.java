package testBash;

import biolockj.Constants;
import biolockj.Log;
import biolockj.module.BioModuleImpl;

public class LongCheckDependencies extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// TODO Auto-generated method stub
		Thread.sleep( 6000 );
	}

	@Override
	public void executeTask() throws Exception {
		// TODO Auto-generated method stub
		Log.info( this.getClass(), "This module does nothing.  "
			+ "It only exists to test the bash scripts and whether they time out in check-dependencies." );
		Thread.sleep( 4000 );
	}
	
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}

	@Override
	public String getDockerImageOwner() {
		return Constants.MAIN_DOCKER_OWNER;
	}

}
