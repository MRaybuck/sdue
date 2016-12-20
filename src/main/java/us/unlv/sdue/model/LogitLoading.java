package us.unlv.sdue.model;

import java.util.ArrayList;

/**
 * Inspired by STOCH Algorithm p.288 of Urban Transportation Networks, Yosef SHEFFI.
 * STOCH Algorithm implements a logit route-choice model at the network level.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class LogitLoading {
	
	// r[i] denotes the travel time from origin node r to node i along the minimum travel time path
	private double[] r;
	// s[i] denotes the travel time from node i to destination node s along the minimum travel time path
	private double[] s;
	// link likelihoods
	private double[][] l;
	// link weights
	private double[][] w;
	// link flows
	private double[][] x;

	/**
	 * Loads link flows to a network according to a logit route-choice model.
	 * @param network the network of interest
	 * @param origin the origin node number
	 * @param destination the destination node number
	 * @param tripRate the flow of trips per time unit
	 * @param theta the parameter used to calculate link likelihoods
	 * @return x the link flows after the loading
	 */
	public double[][] load(Network network, int origin, int destination, int tripRate, double theta) {

		/**********************************/
		/***** Step 0: Preliminaries *****/
		/**********************************/
		
		int numberNodes = network.size();
		r = new double[numberNodes];
		s = new double[numberNodes];
		l = new double[numberNodes][numberNodes];
		w = new double[numberNodes][numberNodes];
		x = new double[numberNodes][numberNodes];

		// [11.4] p.289
		for (int i = 0; i < numberNodes; i++) {
			for (int j = 0; j < numberNodes; j++) {
				r[i] = new PathsCollection(network, origin, i).findShortestPath().getWeight();
				r[j] = new PathsCollection(network, origin, j).findShortestPath().getWeight();
				s[i] = new PathsCollection(network, i, destination).findShortestPath().getWeight();
				s[j] = new PathsCollection(network, j, destination).findShortestPath().getWeight();
				if (network.isEdge(i, j)) {
					if (r[i] < r[j] && s[i] > s[j])
						l[i][j] = Math.exp(theta * (r[j] - r[i] - network.getWeight(i, j)));
					else
						l[i][j] = 0;
				}
			}
		}

		/*******************************/
		/**** Step 1: Forward pass ****/
		/*******************************/

		int currentNodeNumber;
		double sum;
		// set of downstream nodes of all links leaving a node
		ArrayList<Integer> o;
		// set of upstream nodes of all links arriving at a node
		ArrayList<Integer> f;
		
		// Consider nodes in ascending order of r(i)
		int[] ascendingOrder = new int[numberNodes];
		double[] rCopy = r.clone(); // to save r(i) values
		for (int i = 0; i < numberNodes; i++) {
			ascendingOrder[i] = getMinimalValueIndex(rCopy);
			rCopy[ascendingOrder[i]] = Double.MAX_VALUE; // to be refused after
		}
		// For each node i, calculate the link weight
		for (int i = 0; i < numberNodes; i++) {
			currentNodeNumber = ascendingOrder[i];
			o = network.getLeavingNodes(currentNodeNumber);
			for (int j = 0; j < o.size(); j++) {
				// [11.5] p.290
				if (currentNodeNumber == origin)
					w[currentNodeNumber][o.get(j)] = l[currentNodeNumber][o.get(j)];
				else {
					sum = 0;
					f = network.getArrivingNodes(currentNodeNumber);
					for (int k = 0; k < f.size(); k++)
						sum += w[f.get(k)][currentNodeNumber];
					w[currentNodeNumber][o.get(j)] = sum * l[currentNodeNumber][o.get(j)];
				}
			}
		}

		/********************************/
		/**** Step 2: Backward pass ****/
		/********************************/

		// Consider nodes in ascending order of s(i)
		double[] sCopy = s.clone();
		for (int i = 0; i < numberNodes; i++) {
			ascendingOrder[i] = getMinimalValueIndex(sCopy);
			sCopy[ascendingOrder[i]] = Double.MAX_VALUE; // to be refused after
		}
		// For each node i, calculate the link flow
		for (int j = 0; j < numberNodes; j++) {
			currentNodeNumber = ascendingOrder[j];
			f = network.getArrivingNodes(currentNodeNumber);
			for (int i = 0; i < f.size(); i++) {
				// [11.6] p.290
				if (currentNodeNumber == destination) {
					sum = 0;
					for (int k = 0; k < f.size(); k++)
						sum += w[f.get(k)][currentNodeNumber];
					x[f.get(i)][currentNodeNumber] = tripRate * w[f.get(i)][currentNodeNumber] / sum;
				} else {
					o = network.getLeavingNodes(currentNodeNumber);
					x[f.get(i)][currentNodeNumber] = 0;
					sum = 0;
					for (int k = 0; k < f.size(); k++)
						sum += w[f.get(k)][currentNodeNumber];
					for (int l = 0; l < o.size(); l++)
						x[f.get(i)][currentNodeNumber] += x[currentNodeNumber][o.get(l)];
					if (sum == 0)
						x[f.get(i)][currentNodeNumber] = 0;
					else
						x[f.get(i)][currentNodeNumber] = x[f.get(i)][currentNodeNumber] * w[f.get(i)][currentNodeNumber]
								/ sum;
				}
			}
		}
		
		return x;
		
	}
	
	/**
	 * Gets the index of the minimum value of a tab.
	 * @param tab the tab of interest
	 * @return index the index of the minimum value of a tab
	 */
	private int getMinimalValueIndex(double[] tab) {
		int index = 0;
		double min = tab[index];
		for (int i = index; i < tab.length; i++) {
			if (min > tab[i]) {
				min = tab[i];
				index = i;
			}
		}
		return index;
	}

	public double[] getR() {
		return this.r;
	}
	
	public void printR() {
		for (double i : this.r) {
			System.out.print(i + " ");
		}
	}
	
	public String getStringR() {
		String str = "";
		for (double i : this.r) {
			str += "" + i + " ";
		}
		return str;
	}

	public double[] getS() {
		return this.s;
	}
	
	public void printS() {
		for (double i : this.s) {
			System.out.print(i + " ");
		}
	}
	
	public String getStringS() {
		String str = "";
		for (double i : this.s) {
			str += "" + i + " ";
		}
		return str;
	}

	public double[][] getLinkLikelihoods() {
		return this.l;
	}
	
	public void printLinkLikelihoods() {
		for (int i = 0; i < this.l.length; i++) {
			for (int j = 0; j < this.l[i].length; j++) {
				System.out.print(this.l[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public String getStringLinkLikelihoods() {
		String str = "";
		for (int i = 0; i < this.l.length; i++) {
			for (int j = 0; j < this.l[i].length; j++) {
				str += "" + this.l[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}

	public double[][] getLinkWeights() {
		return this.w;
	}
	
	public void printLinkWeights() {
		for (int i = 0; i < this.w.length; i++) {
			for (int j = 0; j < this.w[i].length; j++) {
				System.out.print(this.w[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public String getStringLinkWeights() {
		String str = "";
		for (int i = 0; i < this.w.length; i++) {
			for (int j = 0; j < this.w[i].length; j++) {
				str += "" + this.w[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}

	public double[][] getLinkFlows() {
		return this.x;
	}
	
	public void printLinkFlows() {
		for (int i = 0; i < this.x.length; i++) {
			for (int j = 0; j < this.x[i].length; j++) {
				System.out.print(this.x[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public String getStringLinkFlows() {
		String str = "";
		for (int i = 0; i < this.x.length; i++) {
			for (int j = 0; j < this.x[i].length; j++) {
				str += "" + this.x[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}

}
