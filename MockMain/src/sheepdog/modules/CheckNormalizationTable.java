package sheepdog.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import biolockj.Log;
import biolockj.module.BioModule;
import biolockj.module.BioModuleImpl;
import biolockj.module.report.taxa.BuildTaxaTables;
import biolockj.module.report.taxa.NormalizeTaxaTables;
import biolockj.util.ModuleUtil;
import sheepdog.modules.util.CompareCountTable;
import sheepdog.modules.util.CompareNormalization;

public class CheckNormalizationTable extends BioModuleImpl
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
	
	public List<File> getOutputFiles(String matchingClass ) throws Exception
	{
		List<File> list=  new ArrayList<>();
		
		List<BioModule> mods =  ModuleUtil.getModules(this, false);
		
		for( BioModule bm : mods  )
		{
			if( bm.getClass().getName().equals(matchingClass))
			{
				File outputDir = bm.getOutputDir();
				
				Log.info( getClass(), " TEST_ME module output directory = " +  bm.getClass().getName() + " " + 
								bm.getOutputDir().getAbsolutePath());
						
				String[] files = outputDir.list();
						
				for(String s : files)
				{

					Log.info( getClass(), " TEST_ME module output File = " +  s+ " " + 
									bm.getOutputDir().getAbsolutePath());
							list.add(new File( outputDir.getAbsolutePath() + File.separator + s ));
				}
			}
		}
		
		return list;
	}
	
	@Override
	public void executeTask() throws Exception
	{
		Log.info( getClass(), "IN stub for executeTask()");
		
		List<File> unnormalizedFiles = getOutputFiles(BuildTaxaTables.class.getName());
		List<File> normalizedFiles = getOutputFiles(NormalizeTaxaTables.class.getName());
		
		for(String level : CompareCountTable.LEVELS)
		{
			Log.info( getClass(), "test " + level);
			File aNorm = CheckCountTable.findExactlyOne(level, normalizedFiles);
			File unNorm = CheckCountTable.findExactlyOne(level, unnormalizedFiles);
			
			CompareNormalization.assertEquals(unNorm, aNorm);

			Log.info( getClass(), "pass " + level);
		}
		
		Log.info( getClass(), "Global pass ");
		
	}
}
