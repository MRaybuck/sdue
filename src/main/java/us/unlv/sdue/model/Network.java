package us.unlv.sdue.model;

import java.util.ArrayList;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class Network {

	private int numberNodes;
	private double[][] adjacencyMatrix; 
	private double[][] adjacencyMatrixInitial;

	public Network(int numberNodes) {
		this.numberNodes = numberNodes;
		this.adjacencyMatrix = new double[numberNodes][numberNodes];
		this.adjacencyMatrixInitial = new double[numberNodes][numberNodes];
	}
	
	public Network(double[][] adjacencyMatrix) {
		this.numberNodes = adjacencyMatrix.length;
		this.adjacencyMatrix = adjacencyMatrix;
		// copy adjacencyMatrix in adjacencyMatrixInitial
		double[][] adjacencyMatrixCopy = new double[adjacencyMatrix.length][];
		for (int i = 0; i < adjacencyMatrix.length; i++)
			adjacencyMatrixCopy[i] = adjacencyMatrix[i].clone();
		this.adjacencyMatrixInitial = adjacencyMatrixCopy;
	}
	
	public int getNumberNodes() {
		return this.numberNodes;
	}
	
	public void setNumberNodes(int numberNodes) {
		this.numberNodes = numberNodes;
	}
	
	public double[][] getAdjacencyMatrix() {
		return this.adjacencyMatrix;
	}

	public void setAdjacencyMatrix(double[][] adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
	}

	public double[][] getAdjacencyMatrixInitial() {
		return this.adjacencyMatrixInitial;
	}

	public int size() {
		return this.numberNodes;
	}

	public void addEdge(int source, int target, double weight) {
		this.adjacencyMatrix[source][target] = weight;
	}
	
	public void removeEdge(int source, int target) {
		this.adjacencyMatrix[source][target] = 0;
	}

	public boolean isEdge(int source, int target) {
		return this.adjacencyMatrix[source][target] > 0;
	}
	
	public double getWeight(int source, int target) {
		return this.adjacencyMatrix[source][target];
	}

	public void setWeight(int source, int target, double weight) {
		this.adjacencyMatrix[source][target] = weight;
	}
	
	public void addEdgeInitial(int source, int target, double weight) {
		this.adjacencyMatrixInitial[source][target] = weight;
	}
	
	public void removeEdgeInitial(int source, int target) {
		this.adjacencyMatrixInitial[source][target] = 0;
	}

	public boolean isEdgeInitial(int source, int target) {
		return this.adjacencyMatrixInitial[source][target] > 0;
	}

	public double getWeightInitial(int source, int target) {
		return this.adjacencyMatrixInitial[source][target];
	}

	public void setWeightInitial(int source, int target, double weight) {
		this.adjacencyMatrixInitial[source][target] = weight;
	}
	
	/**
	 * Resets the network. The adjacency matrix becomes again the initial adjacency matrix.
	 */
	public void reset() {
		double[][] adjacencyMatrixCopy = new double[this.adjacencyMatrix.length][];
		for (int i = 0; i < this.adjacencyMatrix.length; i++)
			adjacencyMatrixCopy[i] = this.adjacencyMatrixInitial[i].clone();
		this.adjacencyMatrix = adjacencyMatrixCopy;
	}
	
	/**
	 * Gets an ArrayList of the nodes that can be reached
	 * from nodeNumber by traversing only a single link.
	 * @param nodeNumber the number of the node of interest
	 * @return an ArrayList of the nodes arriving on node number nodeNumber
	 */
	public ArrayList<Integer> getArrivingNodes(int nodeNumber) {
		ArrayList<Integer> arrivingNodes = new ArrayList<Integer>();
		for (int i = 0; i <  size(); i++) 
			if (this.adjacencyMatrix[i][nodeNumber] > 0)
				arrivingNodes.add(i);
		return arrivingNodes;
	}

	/**
	 * Gets an ArrayList of the nodes that can be reached
	 * from nodeNumber by traversing only a single link.
	 * @param nodeNumber the number of the node of interest
	 * @return an ArrayList of the nodes that can be reached
	 * from nodeNumber by traversing only a single link
	 */
	public ArrayList<Integer> getLeavingNodes(int nodeNumber) {
		ArrayList<Integer> leavingNodes = new ArrayList<Integer>();
		for (int j = 0; j < size(); j++) 
			if (this.adjacencyMatrix[nodeNumber][j] > 0)
				leavingNodes.add(j);
		return leavingNodes;
	}

	public void print() {
		for (int i = 0; i < this.adjacencyMatrix.length; i++) {
			for (int j = 0; j < this.adjacencyMatrix[i].length; j++) {
				System.out.print(this.adjacencyMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.adjacencyMatrix.length; i++) {
			for (int j = 0; j < this.adjacencyMatrix[i].length; j++) {
				str += "" + this.adjacencyMatrix[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}
	
}
