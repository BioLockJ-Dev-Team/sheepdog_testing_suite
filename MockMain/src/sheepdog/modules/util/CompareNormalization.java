package sheepdog.modules.util;

import java.io.File;

public class CompareNormalization
{
	public static void assertEquals(File unNormalizedFile, File normalizedFile) throws Exception
	{
		OtuWrapper unnormWrapper = new OtuWrapper(unNormalizedFile);
		
		OtuWrapper normWrapper = new OtuWrapper(normalizedFile);
		
		if(unnormWrapper.getOtuNames().size() != normWrapper.getOtuNames().size())
			throw new Exception("Mismatch on number of taxa " +unnormWrapper.getOtuNames().size() + " " 
						+ normWrapper.getOtuNames().size());
		
		if(unnormWrapper.getSampleNames().size() != normWrapper.getSampleNames().size())
			throw new Exception("Mismatch on sample size");
		
		for( int x=0; x < unnormWrapper.getSampleNames().size(); x++)
		{
			String sampleName = unnormWrapper.getSampleNames().get(x);
			
			for( int y=0; y < unnormWrapper.getOtuNames().size(); y++)
			{
				String taxaName = unnormWrapper.getOtuNames().get(y);
				
				double expectedVal = unnormWrapper.getDataPointsNormalizedThenLogged().get(x).get(y);
				
				double anotherVal = normWrapper.getDataPointsUnnormalized().get(normWrapper.getIndexForSampleName(sampleName))
						.get(normWrapper.getIndexForOtuName(taxaName));
				
				if( Math.abs(  expectedVal - anotherVal ) > 0.0001)
					throw new Exception("Mismatch " + sampleName + " " + taxaName + " " + expectedVal + " " + anotherVal);
			}
		}
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
		
		System.out.println("Global pass");
	}
}
