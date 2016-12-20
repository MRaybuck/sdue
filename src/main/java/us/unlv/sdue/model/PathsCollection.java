package us.unlv.sdue.model;

import java.util.ArrayList;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class PathsCollection {
	
	private Network network; 
	private ArrayList<Path> allPaths = new ArrayList<Path>(); 

	public PathsCollection(Network network, int origin, int destination) {
		this.network = network;
		this.findAllPaths(origin, destination, new Path(network));
	}

	public Network getNetwork() {
		return this.network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public ArrayList<Path> getAllPaths() {
		return this.allPaths;
	}

	public void setAllPaths(ArrayList<Path> allPaths) {
		this.allPaths = allPaths;
	}

	/**
	 * Finds all the paths from the origin to the destination.
	 * @param origin the origin node number
	 * @param destination the target node number
	 * @param path the current recursive path
	 */
	public void findAllPaths(int origin, int destination, Path path) {
		if (origin != destination) {
			path.addNode(origin);
			ArrayList<Integer> leavingNodes = network.getLeavingNodes(origin);
			for (int node : leavingNodes) {
				if (!path.getNodes().contains(node)) {
					this.findAllPaths(node, destination, new Path(path));
				}
			}
		}
		else {
			path.addNode(destination);
			this.allPaths.add(path);
		}
	}
	
	/**
	 * Finds the shortest path of a collection of paths.
	 * @return shortestPath the shortest path of a collection of paths
	 */
	public Path findShortestPath() {
		double minWeight = Double.MAX_VALUE;
		Path shortestPath = new Path(this.network);
		for (Path currentPath : this.allPaths) {
			if (currentPath.getWeight() < minWeight) {
				shortestPath = currentPath;
				minWeight = shortestPath.getWeight();
			}
		}
		return shortestPath;
	}
	
	public int size() {
		return this.allPaths.size();
	}

	public void printAllPaths() {
		for (Path path : this.allPaths) {
			path.print();
			System.out.println();
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		for (Path path : this.allPaths) {
			str += path;
			str += "\n";
		}
		return str;
	}

}
