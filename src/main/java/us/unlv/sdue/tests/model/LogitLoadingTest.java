package us.unlv.sdue.tests.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.unlv.sdue.model.LogitLoading;
import us.unlv.sdue.model.Network;

/**
 * Tests validated thanks to the STOCH Algorithm p.288 of Urban Transportation Networks, Yosef SHEFFI.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class LogitLoadingTest {
	
	private LogitLoading logitLoading;

	@Before
	public void setUp() throws Exception {
		this.logitLoading = new LogitLoading();
	}

	@After
	public void tearDown() throws Exception {
		this.logitLoading = null;
	}
	
	@Test
	public void testLoading() {
		assertNotNull("logitLoading not implemented", this.logitLoading);
	}
	
	@Test
	public void testLoad() {
		double[][] adjacencyMatrix = {{0, 2, 0, 2, 0, 0, 0, 0, 0},
				                      {0, 0, 2, 0, 2, 0, 0, 0, 0},
				                      {0, 0, 0, 0, 0, 2, 0, 0, 0},
				                      {0, 0, 0, 0, 1, 0, 2, 0, 0},
				                      {0, 0, 0, 0, 0, 1, 0, 2, 0},
				                      {0, 0, 0, 0, 0, 0, 0, 0, 2},
				                      {0, 0, 0, 0, 0, 0, 0, 2, 0},
				                      {0, 0, 0, 0, 0, 0, 0, 0, 2},
				                      {0, 0, 0, 0, 0, 0, 0, 0, 0}};			
		Network network = new Network(adjacencyMatrix);
		int origin = 0;
		int destination = 8;
		int tripRate = 1000;
		double theta = 1;
		
		this.logitLoading.load(network, origin, destination, tripRate, theta);
		
		String rExpected = "0.0 2.0 4.0 2.0 3.0 4.0 4.0 5.0 6.0 ";
		assertEquals("Wrong r[]", rExpected, this.logitLoading.getStringR());
		
		String sExpected = "6.0 5.0 4.0 4.0 3.0 2.0 4.0 2.0 0.0 ";
		assertEquals("Wrong s[]", sExpected, this.logitLoading.getStringS());
		
		String linkLikelihoodsExpected = "0.0 1.0 0.0 1.0 0.0 0.0 0.0 0.0 0.0 \n" + 
										 "0.0 0.0 1.0 0.0 0.36787944117144233 0.0 0.0 0.0 0.0 \n" +
										 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
										 "0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 \n" +
										 "0.0 0.0 0.0 0.0 0.0 1.0 0.0 1.0 0.0 \n" +
										 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 1.0 \n" +
										 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.36787944117144233 0.0 \n" +
										 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.36787944117144233 \n" +
										 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n";	
		assertEquals("Wrong link likelihoods", linkLikelihoodsExpected, this.logitLoading.getStringLinkLikelihoods());
		
		String linkWeightsExpected = "0.0 1.0 0.0 1.0 0.0 0.0 0.0 0.0 0.0 \n" + 
									 "0.0 0.0 1.0 0.0 0.36787944117144233 0.0 0.0 0.0 0.0 \n" +
									 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
									 "0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 \n" +
									 "0.0 0.0 0.0 0.0 0.0 1.3678794411714423 0.0 1.3678794411714423 0.0 \n" +
									 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 1.3678794411714423 \n" +
									 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
									 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.503214724408055 \n" +
									 "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n";
		assertEquals("Wrong link weights", linkWeightsExpected, this.logitLoading.getStringLinkWeights());
		
		String linkFlowsExpected = "0.0 268.94142136999517 0.0 731.0585786300048 0.0 0.0 0.0 0.0 0.0 \n" + 
								   "0.0 0.0 0.0 0.0 268.94142136999517 0.0 0.0 0.0 0.0 \n" +
								   "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
								   "0.0 0.0 0.0 0.0 731.0585786300048 0.0 0.0 0.0 0.0 \n" +
								   "0.0 0.0 0.0 0.0 0.0 731.0585786300048 0.0 268.9414213699951 0.0 \n" +
								   "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 731.0585786300048 \n" +
								   "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n" +
								   "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 268.9414213699951 \n" +
								   "0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n";
		assertEquals("Wrong link flows", linkFlowsExpected, this.logitLoading.getStringLinkFlows());
	}

}
