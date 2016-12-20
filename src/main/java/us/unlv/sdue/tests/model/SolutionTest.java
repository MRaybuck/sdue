package us.unlv.sdue.tests.model;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.unlv.sdue.model.Solution;

/**
 * Graphically validated tests.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class SolutionTest {

	private Solution solution;
	
	@Before
	public void setUp() throws Exception {
		this.solution = new Solution();
	}

	@After
	public void tearDown() throws Exception {
		this.solution = null;
	}

	@Test
	public void testSolution() {
		assertNotNull("solution not implemented", this.solution);
	}	
		
}
