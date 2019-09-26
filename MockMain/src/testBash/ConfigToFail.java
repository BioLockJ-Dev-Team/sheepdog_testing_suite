package testBash;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import biolockj.Config;
import biolockj.Log;
import biolockj.exception.BioLockJException;
import biolockj.module.BioModuleImpl;

public class ConfigToFail extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		boolean shouldFail = Config.getBoolean( this, "configToFail.fail" );
		if (shouldFail) {
			Log.info(this.getClass(), "Just fyi... I'm gonna fail.");
		}
	}

	@Override
	public void executeTask() throws Exception {
		File f = new File( getOutputDir() + File.separator + "timeRightNow.txt" );
		FileWriter writer = new FileWriter( f );
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writer.write( dateFormat.format(date) );
		writer.close();
		
		boolean shouldFail = Config.getBoolean( this, "configToFail.fail" );
		if (shouldFail) {
			throw new BioLockJException( "This module is configured to fail." );
		}else {
			Log.info(this.getClass(), "This module is configured to pass.");
		}
	}

}
