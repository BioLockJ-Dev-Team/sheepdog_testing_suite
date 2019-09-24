package testBash;

import biolockj.Log;
import biolockj.exception.BioLockJException;
import biolockj.module.BioModuleImpl;

public class FailCheckDependencies extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// TODO Auto-generated method stub
		throw new BioLockJException( "Helpful message for the user." );
	}

	@Override
	public void executeTask() throws Exception {
		// TODO Auto-generated method stub
		Log.info( this.getClass(), "This module does nothing.  "
						+ "It only exists to test the bash scripts and whether report failures in check-dependencies." );
	}

}
