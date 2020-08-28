package envVar;
import java.io.File;
import java.io.FileWriter;
import biolockj.Config;
import biolockj.Log;
import biolockj.api.ApiModule;
import biolockj.module.JavaModuleImpl;

public class ShowTestVar extends JavaModuleImpl implements ApiModule{
	
	@Override
	public void runModule() throws Exception {
		String val = Config.replaceEnvVar( "${" + getTestVar() + "}" );
		String result = "The variable [" + getTestVar() + "] has the value \"" + val + "\"." + System.lineSeparator();
		Log.info(ShowTestVar.class, result);
		
		FileWriter writer = new FileWriter( new File(getOutputDir(), "testVarVal.txt") );
		writer.write( result );
		writer.close();
	}

	@Override
	public String getDescription() {
		return "This module gets the value of the environment variable \"" 
						+ getTestVar() + "\" and prints its value to the log and an output file." + System.lineSeparator()
						+ "The variable must be set in the environment using the local system or the -e parameter to BioLockJ.";
	}
	
	protected String getTestVar() {
		return "TEST_VAR";
	}

	@Override
	public String getCitationString() {
		return "BioModule created by Ivory Blakley as part of the test suite for BioLockJ.";
	}

}
