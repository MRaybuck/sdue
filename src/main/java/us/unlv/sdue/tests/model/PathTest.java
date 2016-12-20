package us.unlv.sdue.tests.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import us.unlv.sdue.model.Network;
import us.unlv.sdue.model.Path;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class PathTest extends TestCase {

	private Path path1;
	private Path path2;
	
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
		
		this.path1 = new Path(network);
		this.path1.addNode(0);
		this.path1.addNode(1);
		this.path1.addNode(4);
		this.path1.addNode(5);
		this.path2 = new Path(path1);
	}

	@After
	public void tearDown() throws Exception {
		this.path1 = null;
		this.path2 = null;
	}
	
	@Test
	public void testPath1() {
		assertNotNull("path1 not implemented", this.path1);
	}
	
	@Test
	public void testPath2() {
		assertNotNull("path2 not implemented", this.path2);
	}

	@Test
	public void testNotSame() {
		assertNotSame("path1 and path2 refer the same object", path1, path2);
	}
	
	@Test
	public void testGetOrigin() {
		assertEquals("Wrong origin", 0, this.path1.getOrigin());
		assertEquals("Wrong origin", 0, this.path2.getOrigin());
	}
	
	@Test
	public void testGetDestination() {
		assertEquals("Wrong destination", 5, this.path1.getDestination());
		assertEquals("Wrong destination", 5, this.path2.getDestination());
	}
	
	@Test
	public void testGetWeight() {
		assertEquals("Wrong weight", 5.0, this.path1.getWeight());
		assertEquals("Wrong weight", 5.0, this.path2.getWeight());
	}
	
	@Test
	public void testToString() {
		assertEquals("Wrong string path1", "0 1 4 5 ", this.path1.toString());
		assertEquals("Wrong string path2", "0 1 4 5 ", this.path2.toString());
	}
	
	@Test
	public void testAddNode() {
		this.path1.addNode(2);
		assertEquals("Should not add node", "0 1 4 5 ", this.path1.toString());
		this.path2.addNode(8);
		assertEquals("Should add node", "0 1 4 5 8 ", this.path2.toString());
		assertEquals("Wrong new destination", 8, this.path2.getDestination());
		assertEquals("Wrong new weight", 7.0, this.path2.getWeight());
		this.path2.addNode(7);
		assertEquals("Should not add node", "0 1 4 5 8 ", this.path2.toString());
	}

}
