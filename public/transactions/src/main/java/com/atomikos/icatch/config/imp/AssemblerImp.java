package com.atomikos.icatch.config.imp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.atomikos.icatch.config.Assembler;
import com.atomikos.icatch.config.ConfigProperties;
import com.atomikos.logging.LoggerFactory;
import com.atomikos.util.ClassLoadingHelper;

public class AssemblerImp implements Assembler {
	
	private static final String DEFAULT_PROPERTIES_FILE_NAME = "transactions-defaults.properties";
	
	private static com.atomikos.logging.Logger LOGGER = LoggerFactory.createLogger(AssemblerImp.class);
	
    private Properties loadPropertiesFromClasspath(String fileName){
    		Properties p = new Properties();
    		URL url = null;
    		
    		//first look in application classpath (cf ISSUE 10091)
    		url = ClassLoadingHelper.loadResourceFromClasspath(getClass(), fileName);		
    		if (url == null) {
    			url = getClass().getClassLoader().getSystemResource ( fileName );
    		}
    		if (url != null) {
    			LOGGER.logWarning ( "Using init file: " + url.getPath() );
    			InputStream in;
				try {
					in = url.openStream();
					p.load ( in );
					in.close();
				} catch (IOException e) {
					LOGGER.logWarning("Failed to load properties", e);
				}
    		}
            return p;
    }

	/**
	 * Called by ServiceLoader.
	 */
	public AssemblerImp() {
		
	}

	@Override
	public ConfigProperties getConfigProperties() {
		return new ConfigProperties(loadPropertiesFromClasspath(DEFAULT_PROPERTIES_FILE_NAME));
	}
}
