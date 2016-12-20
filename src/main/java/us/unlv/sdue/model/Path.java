package us.unlv.sdue.model;

import java.util.ArrayList;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class Path {

	private Network network; 
	private ArrayList<Integer> nodes; 

	public Path(Network network) {
		this.network = network;
		this.nodes = new ArrayList<>(); 
	}

	/**
	 * Constructs a duplicated path.
	 * @param path the path to duplicate
	 */
	@SuppressWarnings("unchecked")
	public Path(Path path) {
		this.network = path.network;
		this.nodes = (ArrayList<Integer>) path.nodes.clone();
	}

	public Network getNetwork() {
		return this.network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public ArrayList<Integer> getNodes() {
		return this.nodes;
	}

	public void setNodes(ArrayList<Integer> nodes) {
		this.nodes = nodes;
	}

	public int getOrigin() {
		return this.nodes.get(0);
	}
	
	public int getDestination() {
		return this.nodes.get(this.nodes.size()-1);
	}
	
	public int size() {
		return this.nodes.size();
	}
	
	/**
	 * Gets the total weight of the path.
	 * @return weight the total weight of the path
	 */
	public double getWeight() {
		double weight = 0;
		for (int i = 0; i < this.size() - 1; i++) {
			weight += this.network.getAdjacencyMatrix()[this.nodes.get(i)][this.nodes.get(i + 1)];
		}
		return weight;
	}

	/**
	 * Adds a node to the path.
	 * @param node the node to add to the path
	 */
	public void addNode(int node) {
		if (!this.nodes.isEmpty()) {
			if (this.network.getAdjacencyMatrix()[this.nodes.get(this.nodes.size() - 1)][node] > 0) {
				this.nodes.add(node);
			} else {
				System.err.println("Can't add node");
			}
		} else {
			this.nodes.add(node);
		}
	}

	public void print() {
		for (int i : this.nodes) {
			System.out.print(i + " ");
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (int i : this.nodes) {
			str += "" + i + " ";
		}
		return str;
	}

}
