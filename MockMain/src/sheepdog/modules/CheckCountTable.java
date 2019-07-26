package sheepdog.modules;

import java.io.File;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModule;
import biolockj.module.BioModuleImpl;
import biolockj.module.classifier.ClassifierModule;
import biolockj.util.ModuleUtil;

public class CheckCountTable extends BioModuleImpl
{
	@Override
	public void checkDependencies() throws Exception
	{
		Log.info( getClass(), "IN stub for checkDependencies()");
		
	}
	
	@Override
	public boolean isValidInputModule(BioModule module)
	{
		return false;
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for executeTask()");
		
		ClassifierModule cm =  ModuleUtil.getClassifier(this, false);
		
		Log.info( getClass(), " TEST_ME module output directory = " +  cm.getOutputDir().getAbsolutePath());
		
		List<File> inputFiles = getInputFiles();
		
		for( File f : inputFiles)
			Log.info( getClass(), " TEST_ME input file = " +  f.getAbsolutePath());
		
	}
}
