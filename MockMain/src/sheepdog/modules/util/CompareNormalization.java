package sheepdog.modules.util;

import java.io.File;

public class CompareNormalization
{
	public static void assertEquals(File unNormalizedFile, File normalizedFile) throws Exception
	{
		OtuWrapper unnormWrapper = new OtuWrapper(unNormalizedFile);
		
		OtuWrapper normWrapper = new OtuWrapper(normalizedFile);
	}
	
	public static void main(String[] args) throws Exception
	{
		for(String level : CompareCountTable.LEVELS )
		{
			System.out.println(level);
			File norm = new File(
					"C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\" + 
			"reparseKraken2Parser_2019Jul29\\05_NormalizeTaxaTables\\output\\reparseKraken2Parser_2019Jul29_taxaCount_norm_Log10_" 
							+ level +  ".tsv");
			
			File unNorm = new File("C:\\sheepDog\\sheepdog_testing_suite\\MockMain\\pipelines\\reparseKraken2Parser_2019Jul29\\03_BuildTaxaTables\\output\\" +
					 "reparseKraken2Parser_2019Jul29_taxaCount_" + level + ".tsv");
			
			assertEquals(unNorm, norm);
			
		}
	}
}
