package sheepdog.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import biolockj.Config;
import biolockj.Log;
import biolockj.api.ApiModule;
import biolockj.module.BioModule;
import biolockj.module.ScriptModuleImpl;
import biolockj.util.ModuleUtil;

public class CheckPdfAndTemp extends ScriptModuleImpl implements ApiModule {

	@Override
	public List<List<String>> buildScript( List<File> files ) throws Exception {
		BioModule prev = ModuleUtil.getPreviousModule( this );
		Log.info(getClass(), "Found previous module: " + ModuleUtil.displaySignature( prev ));
		Log.info(getClass(), "Copying previous temp files this modules output dir...");
		for( File tempFile: prev.getTempDir().listFiles() ) {
			if( tempFile.isFile() ) {
				Log.info(getClass(), "Copying file: " + tempFile.getName() );
				FileUtils.copyFileToDirectory( tempFile, getOutputDir() );
			}
		}

		List<List<String>> outer = new ArrayList<>();
		List<String> inner = new ArrayList<>();
		for( File outFile: prev.getOutputDir().listFiles() ) {
			if( outFile.getName().endsWith( ".pdf" ) ) {
				Log.info(getClass(), "Found pdf file: " + outFile.getName() );
				inner.add( Config.getExe( this, "exe.pdftoppm" ) + " " + outFile.getAbsolutePath() + " " +
					getOutputDir().getAbsolutePath() + File.separator + outFile.getName() + "_image" );
			}
		}
		outer.add( inner );
		return outer;
	}

	@Override
	public String getDescription() {
		return "Make an image of each page of each pdf output from the previous module";
	}

	@Override
	public String getCitationString() {
		return "Module created by Ivory Blakley for the sheepdog testing suite";
	}

	@Override
	public String getDockerImageName() {
		return "pdftoppm";
	}

	@Override
	public String getDockerImageTag() {
		return "latest";
	}

}
