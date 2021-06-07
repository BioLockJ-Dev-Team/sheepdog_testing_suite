package sheepdog.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import biolockj.Constants;
import biolockj.Log;
import biolockj.module.BioModule;
import biolockj.module.BioModuleImpl;
import biolockj.module.report.taxa.BuildTaxaTables;
import biolockj.util.ModuleUtil;
import sheepdog.modules.util.CompareCountTable;

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
	
	public static File findExactlyOne(String level, List<File> list) throws Exception
	{
		File aFile = null;
		
		for(File f : list)
		{
			if( f.getName().indexOf(level) != - 1)
			{
				if( aFile != null)
					throw new Exception("Found duplicate for " + level);
				
				aFile = f;
			}
		}
		
		if( aFile == null)
			throw new Exception("Could not find " + level);
		
		return aFile;
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for executeTask()");
		
		List<BioModule> mods =  ModuleUtil.getModules(this, false);
		
		List<File> countTables = new ArrayList<>();
		
		List<File> inputFiles =null;
		
		for( BioModule bm : mods)
		{
			if( bm.getClass().getName().equals(BuildTaxaTables.class.getName()))
			{
				File outputDir = bm.getOutputDir();
				
				Log.info( getClass(), " TEST_ME module output directory = " +  bm.getClass().getName() + " " + 
						bm.getOutputDir().getAbsolutePath());
				
				String[] files = outputDir.list();
				
				for(String s : files)
				{

					Log.info( getClass(), " TEST_ME module output File = " +  s+ " " + 
							bm.getOutputDir().getAbsolutePath());
					countTables.add(new File( outputDir.getAbsolutePath() + File.separator + s ));
				}
				
				inputFiles= bm.getInputFiles();
			}
		}
		
		File parentDir = null;
		
		for( File f : inputFiles)
		{
			Log.info( getClass(), " TEST_ME input file = " +  f.getAbsolutePath());
			
			if(parentDir == null)
			{
				parentDir = f.getParentFile();
			}
			else
			{
				if( ! parentDir.equals(f.getParentFile()))
					throw new Exception("Inconsistent parent directory " + parentDir + " " + f.getParentFile());
			}
		}
			
		
		for(String level : CompareCountTable.LEVELS)
		{
			File countTable = findExactlyOne(level, countTables);
			
			Log.info( getClass(), " TEST_ME check  " + countTable.getAbsolutePath());
			CompareCountTable.assertEquals(parentDir, countTable, level);

			Log.info( getClass(), " TEST_ME pass " + countTable.getAbsolutePath());
		}
		

		Log.info( getClass(), "Global pass ");
		
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
