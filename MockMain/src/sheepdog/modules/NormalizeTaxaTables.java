package sheepdog.modules;

import biolockj.Constants;
import biolockj.Log;
import biolockj.exception.BioLockJException;
import biolockj.module.JavaModuleImpl;

public class NormalizeTaxaTables extends JavaModuleImpl {

	@Override
	public void runModule() throws Exception {
		Log.error(getClass(), "This module does nothing.  It's not nearly as good as the real NormalizeTaxaTables module.");
		throw new SpecifiedClassExc("The class [sheepdog.modules.NormalizeTaxaTables] only exists for testing purposes." + System.lineSeparator()
		+ "If you actually want to normalize data, you probably want:" + System.lineSeparator()
		+ Constants.BLJ_MODULE_TAG + " biolockj.module.report.taxa.NormalizeTaxaTables");
	}

	private class SpecifiedClassExc extends BioLockJException {

		public SpecifiedClassExc( String msg ) {
			super( msg );
		}
		
	}
	
}


