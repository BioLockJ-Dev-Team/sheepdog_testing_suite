package envVar;

import java.io.File;
import java.io.FileWriter;
import biolockj.Config;
import biolockj.Log;

public class ShowBljVars extends ShowTestVar {

	String TEST_VAR1 = "BLJ";
	String TEST_VAR2 = "BLJ_PROJ";
	
	@Override
	public void runModule() throws Exception {
		String val1 = Config.replaceEnvVar( "${" + TEST_VAR1 + "}" );
		String val2 = Config.replaceEnvVar( "${" + TEST_VAR2 + "}" );
		String result1 = "The variable [" + TEST_VAR1 + "] has the value \"" + val1 + "\"." + System.lineSeparator();
		String result2 = "The variable [" + TEST_VAR2 + "] has the value \"" + val2 + "\"." + System.lineSeparator();
		Log.info(ShowTestVar.class, result1);
		Log.info(ShowTestVar.class, result2);
		
		FileWriter writer = new FileWriter( new File(getOutputDir(), "testVarVal.txt") );
		writer.write( result1 );
		writer.write( result2 );
		writer.close();
	}

	@Override
	public String getDescription() {
		return "This module tests that the variables \"" 
						+ TEST_VAR1 + "\" and \"" + TEST_VAR2 + "\" "
						+ "have values and prints the values to the log and an output file.";
	}
}
