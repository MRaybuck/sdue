package us.unlv.sdue.tests.model;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import us.unlv.sdue.model.Network;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class NetworkTest extends TestCase {
	
	private Network network1;
	private Network network2;

	@Before
	public void setUp() throws Exception {
		int numberNodes = 9;
		this.network1 = new Network(numberNodes);
		this.network1.addEdge(0, 1, 2);
		this.network1.addEdge(0, 3, 2);
		this.network1.addEdge(1, 2, 2);
		this.network1.addEdge(1, 4, 2);
		this.network1.addEdge(2, 5, 2);
		this.network1.addEdge(3, 4, 1);
		this.network1.addEdge(3, 6, 2);
		this.network1.addEdge(4, 5, 1);
		this.network1.addEdge(4, 7, 2);
		this.network1.addEdge(5, 8, 2);
		this.network1.addEdge(6, 7, 2);
		this.network1.addEdge(7, 8, 2);
		this.network1.addEdgeInitial(0, 1, 2);
		this.network1.addEdgeInitial(0, 3, 2);
		this.network1.addEdgeInitial(1, 2, 2);
		this.network1.addEdgeInitial(1, 4, 2);
		this.network1.addEdgeInitial(2, 5, 2);
		this.network1.addEdgeInitial(3, 4, 1);
		this.network1.addEdgeInitial(3, 6, 2);
		this.network1.addEdgeInitial(4, 5, 1);
		this.network1.addEdgeInitial(4, 7, 2);
		this.network1.addEdgeInitial(5, 8, 2);
		this.network1.addEdgeInitial(6, 7, 2);
		this.network1.addEdgeInitial(7, 8, 2);
		
		double[][] adjacencyMatrix = {{0, 2, 0, 2, 0, 0, 0, 0, 0},
									  {0, 0, 2, 0, 2, 0, 0, 0, 0},
									  {0, 0, 0, 0, 0, 2, 0, 0, 0},
									  {0, 0, 0, 0, 1, 0, 2, 0, 0},
									  {0, 0, 0, 0, 0, 1, 0, 2, 0},
									  {0, 0, 0, 0, 0, 0, 0, 0, 2},
									  {0, 0, 0, 0, 0, 0, 0, 2, 0},
									  {0, 0, 0, 0, 0, 0, 0, 0, 2},
									  {0, 0, 0, 0, 0, 0, 0, 0, 0}};			
		this.network2 = new Network(adjacencyMatrix);
	}

	@After
	public void tearDown() throws Exception {
		this.network1 = null;
		this.network2 = null;
	}

	@Test
	public void testNetwork1() {
		assertNotNull("network1 not implemented", this.network1);
	}
	
	@Test
	public void testNetwork2() {
		assertNotNull("network1 not implemented", this.network2);
	}
	
	@Test
	public void testSize() {
		assertEquals("Wrong size network1", 9, this.network1.size());
		assertEquals("Wrong size network2", 9, this.network2.size());
	}
	
	@Test
	public void testSetWeight() {
		this.network1.setWeight(0, 1, 3);
		this.network2.setWeight(0, 1, 3);
		double weightExpected = 3;
		double weightInitialExpected = 2;
		assertEquals("Wrong weight network1", weightExpected, this.network1.getWeight(0, 1));
		assertEquals("Wrong weight network2", weightExpected, this.network2.getWeight(0, 1));
		assertEquals("Wrong weight initial network1", weightInitialExpected, this.network1.getWeightInitial(0, 1));
		assertEquals("Wrong weight initial network2", weightInitialExpected, this.network2.getWeightInitial(0, 1));
	}
	
	@Test
	public void testReset() {
		this.network1.setWeight(0, 1, 3);
		this.network2.setWeight(0, 1, 3);
		this.network2.reset();
		double weightNetwork1Expected = 3;
		double weightNetwork2Expected = 2;
		assertEquals("Wrong weight network1", weightNetwork1Expected, this.network1.getWeight(0, 1));
		assertEquals("Wrong weight network2", weightNetwork2Expected, this.network2.getWeight(0, 1));
	}
	
	@Test
	public void testGetArrivingNodes() {
		ArrayList<Integer> arrayListExpected = new ArrayList<Integer>();
		arrayListExpected.add(1);
		arrayListExpected.add(3);
		assertEquals("Wrong arriving nodes network1", arrayListExpected, this.network1.getArrivingNodes(4));
		assertEquals("Wrong arriving nodes network2", arrayListExpected, this.network2.getArrivingNodes(4));
	}
	
	@Test
	public void testGetLeavingNodes() {
		ArrayList<Integer> arrayListExpected = new ArrayList<Integer>();
		arrayListExpected.add(5);
		arrayListExpected.add(7);
		assertEquals("Wrong leaving nodes network1", arrayListExpected, this.network1.getLeavingNodes(4));
		assertEquals("Wrong leaving nodes network2", arrayListExpected, this.network2.getLeavingNodes(4));
	}
	
	@Test
	public void testToString() {
		String stringExpected = "0.0 2.0 0.0 2.0 0.0 0.0 0.0 0.0 0.0 \n" + 
								"0.0 0.0 2.0 0.0 2.0 0.0 0.0 0.0 0.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 2.0 0.0 0.0 0.0 \n" +
								"0.0 0.0 0.0 0.0 1.0 0.0 2.0 0.0 0.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 1.0 0.0 2.0 0.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 2.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 0.0 0.0 2.0 0.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 2.0 \n" +
								"0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 \n";	
		assertEquals("Wrong string network1", stringExpected, this.network1.toString());
		assertEquals("Wrong string network2", stringExpected, this.network2.toString());
	}

}
