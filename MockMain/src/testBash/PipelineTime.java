package testBash;

import biolockj.Config;
import biolockj.Constants;
import biolockj.Log;
import biolockj.Properties;
import biolockj.api.ApiModule;
import biolockj.module.BioModuleImpl;
import biolockj.util.SummaryUtil;

public class PipelineTime extends BioModuleImpl implements ApiModule {
	
	public PipelineTime() {
		addNewProperty( MIN_MILLIS, Properties.INTEGER_TYPE, "If the pipeline reaches this point in fewer than this many milliseconds, throw exception.", "0" );
		addNewProperty( MAX_MILLIS, Properties.INTEGER_TYPE, "If the pipeline takes longer than this many milliseconds to reach this point, throw exception.", Integer.toString( Integer.MAX_VALUE ) );
	}

	@Override
	public String getDockerImageName() {
		return "biolockj_controller";
	}

	@Override
	public void checkDependencies() throws Exception {
		Config.getIntegerProp( this, MIN_MILLIS );
		Config.getPositiveInteger( this, MAX_MILLIS );
	}

	@Override
	public void executeTask() throws Exception {
		final long duration = System.currentTimeMillis() - Constants.APP_START_TIME;
		Log.info(PipelineTime.class, "Runtime up to now: " + SummaryUtil.getRunTime( duration ) );
		Log.info(PipelineTime.class, "Runtime up to now (in millis): " + duration );
		Long max = new Long(Config.getPositiveInteger( this, MAX_MILLIS ));
		if ( max != null && duration > Config.getPositiveInteger( this, MAX_MILLIS ) ) {
			throw new Exception("Pipeline took LONGER than expected to get to this point. " + System.lineSeparator() 
			+ "Max: " + Config.getPositiveInteger( this, MAX_MILLIS ) + System.lineSeparator()
			+ "Actual: " + duration);
		}
		if ( duration < Config.getIntegerProp( this, MIN_MILLIS ) ) {
			throw new Exception("Pipeline took LESS TIME than expected to get to this point. " + System.lineSeparator() 
			+ "Min: " + Config.getPositiveInteger( this, MIN_MILLIS ) + System.lineSeparator()
			+ "Actual: " + duration);
		}
	}

	private static final String MIN_MILLIS = "pipelineTime.minMillis";
	private static final String MAX_MILLIS = "pipelineTime.maxMillis";
	@Override
	public String getDescription() {
		return "This module allows for tests that fail a pipeline if it takes to long or finishes too quickly.";
	}

	@Override
	public String getCitationString() {
		return "Module developed by Ivory Blakley.";
	}
	
}
