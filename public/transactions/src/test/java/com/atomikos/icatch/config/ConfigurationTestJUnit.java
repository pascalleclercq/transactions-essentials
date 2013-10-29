package com.atomikos.icatch.config;


import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationTestJUnit {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindAssemblerInClasspath() {
		Assembler assembler = Configuration.getAssembler();
		Assert.assertNotNull(assembler);
	}
	
	@Test
	public void testDefaultValueForMaxActives() {
		ConfigProperties props = Configuration.getConfigProperties();
		Assert.assertNotNull(props.getProperty("com.atomikos.icatch.max_actives"));
	}

}
