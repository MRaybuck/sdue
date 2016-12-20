package us.unlv.sdue.tests.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import us.unlv.sdue.model.Network;
import us.unlv.sdue.model.Path;
import us.unlv.sdue.model.PathsCollection;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class PathsCollectionTest extends TestCase {
	
	private PathsCollection pathsCollection;

	@Before
	public void setUp() throws Exception {
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
		
		this.pathsCollection = new PathsCollection(network, 0, 8);
	}

	@After
	public void tearDown() throws Exception {
		this.pathsCollection = null;
	}
	
	@Test
	public void testPathCollection() {
		assertNotNull("pathsCollection not implemented", this.pathsCollection);
	}
	
	@Test
	public void testToString() {
		String stringExpected = "0 1 2 5 8 \n" + 
								"0 1 4 5 8 \n" +
								"0 1 4 7 8 \n" +
								"0 3 4 5 8 \n" +
								"0 3 4 7 8 \n" +
								"0 3 6 7 8 \n";	
		assertEquals("Wrong string pathsCollection", stringExpected, this.pathsCollection.toString());
	}
	
	@Test
	public void testSize() {
		assertEquals("Wrong size", 6, this.pathsCollection.size());
	}
	
	@Test
	public void testFindShortestPath() {
		Path shortestPath = this.pathsCollection.findShortestPath();
		String stringExpected = "0 3 4 5 8 ";
		assertEquals("Wrong shortest path", stringExpected, shortestPath.toString());
	}

}
