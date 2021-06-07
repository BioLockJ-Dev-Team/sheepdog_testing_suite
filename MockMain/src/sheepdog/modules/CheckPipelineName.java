package sheepdog.modules;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import biolockj.BioLockJ;
import biolockj.Config;
import biolockj.Constants;
import biolockj.exception.BioLockJException;
import biolockj.module.BioModuleImpl;
import biolockj.util.BioLockJUtil;

public class CheckPipelineName extends BioModuleImpl {

	@Override
	public void checkDependencies() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void executeTask() throws Exception {
		String intendedName = Config.getString( this, NAME_BASE );
		String pipeName = BioLockJ.getPipelineDir().getName();
		
		final String year = String.valueOf( new GregorianCalendar().get( Calendar.YEAR ) );
		final String month = new GregorianCalendar().getDisplayName( Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH );
		final String day = BioLockJUtil.formatDigits( new GregorianCalendar().get( Calendar.DATE ), 2 );
		final String dateString = year + month + day;
		
		intendedName = intendedName.replace( "{DATE}", dateString );		
		
		if ( ! pipeName.equals( intendedName )) {
			throw new PipelineNameException("This module is for testing; this test failed."
				+ System.lineSeparator() + "Expected pipeline name to be: " + intendedName 
				+ System.lineSeparator() + "Found: " + pipeName
				+ System.lineSeparator());
		}
	}
	
	@Override
	public String getDockerImageName() {
		return Constants.MAIN_DOCKER_IMAGE;
	}

	@Override
	public String getDockerImageOwner() {
		return Constants.MAIN_DOCKER_OWNER;
	}
	
	private static final String NAME_BASE = "checkPipelineName.nameBase";

}

class PipelineNameException extends BioLockJException {

	public PipelineNameException( String msg ) {
		super( msg );
	}
	
	private static final long serialVersionUID = 1L;
}
