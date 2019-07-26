package sheepdog.modules;

import java.io.File;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModule;
import biolockj.module.BioModuleImpl;
import biolockj.module.implicit.parser.wgs.Kraken2Parser;
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
		
		List<BioModule> mods =  ModuleUtil.getModules(this, false);
		
		for( BioModule bm : mods)
		{
			if( bm.getClass().getName().equals(Kraken2Parser.class.getName()))
			{
				File outputDir = bm.getOutputDir();
				
				Log.info( getClass(), " TEST_ME module output directory = " +  bm.getClass().getName() + " " + 
						bm.getOutputDir().getAbsolutePath());
				
				String[] files = outputDir.list();
				
				for(String s : files)
				{

					Log.info( getClass(), " TEST_ME module output File = " +  s+ " " + 
							bm.getOutputDir().getAbsolutePath());
				}
			}
		}
		
		List<File> inputFiles = getInputFiles();
		
		for( File f : inputFiles)
			Log.info( getClass(), " TEST_ME input file = " +  f.getAbsolutePath());
		
	}
}
