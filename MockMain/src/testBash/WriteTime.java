package testBash;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import biolockj.Constants;
import biolockj.module.BioModuleImpl;

public class WriteTime extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// nope
	}

	@Override
	public void executeTask() throws Exception {
		File f = new File( getOutputDir() + File.separator + "timeRightNow.txt" );
		FileWriter writer = new FileWriter( f );
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writer.write( dateFormat.format(date) );
		writer.close();
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
