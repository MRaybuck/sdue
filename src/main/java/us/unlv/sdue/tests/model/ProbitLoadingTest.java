package us.unlv.sdue.tests.model;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.unlv.sdue.model.ProbitLoading;

/**
 * Graphically validated tests.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class ProbitLoadingTest {

private ProbitLoading probitLoading;
	
	@Before
	public void setUp() throws Exception {
		this.probitLoading = new ProbitLoading();
	}

	@After
	public void tearDown() throws Exception {
		this.probitLoading = null;
	}

	@Test
	public void testSolution() {
		assertNotNull("probitLoading not implemented", this.probitLoading);
	}	

}
