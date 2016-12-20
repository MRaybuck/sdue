package us.unlv.sdue.model;

import java.util.ArrayList;

/**
 * Inspired by Method of Successive Averages Algorithm p.327 of Urban Transportation Networks, Yosef SHEFFI. 
 * Method of Successive Averages Algorithm is based on a predetermined move size along the descent direction.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class Solution {

	private Network network;
	private ArrayList<double[]> timesData;
	private ArrayList<double[]> flowsData;
	private ArrayList<double[][]> linkFlowsData;

	/**
	 * Runs the simulation to generate a collection of travel times for each path 
	 * according to the Method of Successive Average Algorithm.
	 * @param network the network of interest
	 * @param currentModel the current model selected
	 * @param origin the origin node number
	 * @param destination the destination node number
	 * @param tripRate the flow of trips per time unit
	 * @param numberIterations the number of simulation iterations performed
	 * @param parameter the parameter used in the loadings
	 * @return timesData the collection of travel times for each path
	 */
	public ArrayList<double[]> run(Network network, String currentModel, int origin, int destination, int tripRate,
			int numberIterations, double parameter) {

		/*******************************/
		/*** Step 0 : Initialization ***/
		/*******************************/
		
		this.network = network;
		// Reset because network is changed when run method is executed
		this.network.reset();
		int numberNodes = this.network.getNumberNodes();
		
		// link flows
		double[][] x = new double[numberNodes][numberNodes];
		// auxiliary link flows
		double[][] y = new double[numberNodes][numberNodes];
		// weight of the edges
		double travelTime;
		// collection of travel times for each path
		this.timesData = new ArrayList<double[]>();
		// collection of flows for each path
		this.flowsData = new ArrayList<double[]>();
		// collection of link flows for each edge
		this.linkFlowsData = new ArrayList<double[][]>();

		// Perform a stochastic network loading based on a set of initial travel times
		x = loadModel(currentModel, origin, destination, tripRate, parameter);
		
		// Set n = 1
		double n = 1;

		// Repeat the simulation
		while (n <= numberIterations) { 

			/**********************************/
			/******** Step 1 : Update *********/
			/**********************************/
			
			for (int i = 0; i < numberNodes; i++) {
				for (int j = 0; j < numberNodes; j++) {
					// Set t = f(x) with function f given
					travelTime = this.network.getWeightInitial(i, j)
							* (1 + Math.pow(x[i][j], 4) / (Math.pow(800, 4)));
					this.network.setWeight(i, j, travelTime);
				}
			}

			/**********************************/
			/*** Step 2 : Direction finding ***/
			/**********************************/

			// Perform a stochastic network loading procedure based on the current set of link travel times		
			y = loadModel(currentModel, origin, destination, tripRate, parameter);
		
			/*****************************/
			/******* Step 3 : Move *******/
			/*****************************/

			for (int i = 0; i < numberNodes; i++) {
				for (int j = 0; j < numberNodes; j++) {
					// Calculate the average flow
					x[i][j] = x[i][j] + (1 / n) * (y[i][j] - x[i][j]);
				}
			}

			/************************************/
			/** Step 4 : Convergence criterion **/
			/************************************/
			
			PathsCollection pathsCollection = new PathsCollection(this.network, origin, destination);
			int numberPaths = pathsCollection.getAllPaths().size();
			// current travel times for each path
			double[] travelTimes = new double[numberPaths];
			// current probabilities for each path
			double[] probabilities = new double[numberPaths];
			// current flows for each path
			double[] flows = new double[numberPaths];
			// useful to calculate the probabilities
			double sum = 0;
			for (int i = 0; i < numberPaths; i++) {
				travelTimes[i] = pathsCollection.getAllPaths().get(i).getWeight();
				sum += Math.exp(- parameter * travelTimes[i]);
			}
			// [11.10] p.292 
			for (int i = 0; i < numberPaths; i++) {
				probabilities[i] = Math.exp(- parameter * travelTimes[i]) / sum;
				flows[i] = probabilities[i] * tripRate;
			}
			
			this.timesData.add(travelTimes);
			this.flowsData.add(flows);
			double[][] xCopy = new double[x.length][];
			for (int i = 0; i < x.length; i++)
				xCopy[i] = x[i].clone();
			this.linkFlowsData.add(xCopy);
			n++;

		}
		
		return timesData;
		
	}
	
	/**
	 * Performs a stochastic network loading procedure according to the model selected. 		
	 * @param currentModel the current model selected
	 * @param origin the origin node number
	 * @param destination the destination node number
	 * @param tripRate the flow of trips per time unit
	 * @param parameter the parameter used in the loadings
	 * @return linkFlows the link flows after the loading
	 */
	private double[][] loadModel(String currentModel, int origin, int destination, int tripRate, double parameter) {
		LogitLoading logitLoading = new LogitLoading();
		ProbitLoading probitLoading = new ProbitLoading();
		MixedLogitLoading mixedLogitLoading = new MixedLogitLoading();
		double[][] linkFlows = null;
		switch (currentModel) {
			case "Logit":
				linkFlows = logitLoading.load(this.network, origin, destination, tripRate, parameter);
				break;
			case "Probit":
				linkFlows = probitLoading.load(this.network, origin, destination, tripRate, parameter);
				break;
			case "Mixed-Logit":
				linkFlows = mixedLogitLoading.load(this.network, this.network, origin, destination, tripRate);
				break;
			default:
		}
		return linkFlows;
	}
	
	/**
	 * Gets the String of the collection of travel times for each path.
	 * @return str the String of the collection of travel times for each path
	 */
	public String getStringTimesData() {
		String str = "";
		str += "Path | Iterations\n\n";
		for (int j = 0; j < this.timesData.get(0).length; j++) {
			str += "(" + (j + 1) + ")" + " ";
			for (int i = 0; i < this.timesData.size(); i++)
				str += this.timesData.get(i)[j] + " ";
			str += "\n\n";
		}
		return str;
	}
	
	/**
	 * Gets the String of the collection of flows for each path.
	 * @return str the String of the collection of flows for each path
	 */
	public String getStringFlowsData() {
		String str = "";
		str += "Path | Iterations\n\n";
		for (int j = 0; j < this.flowsData.get(0).length; j++) {
			str += "Path: (" + (j + 1) + ")" + " ";
			for (int i = 0; i < this.flowsData.size(); i++)
				str += this.flowsData.get(i)[j] + " ";
			str += "\n\n";
		}
		return str;
	}
	
	/**
	 * Gets the String of the collection of link flows for each edge.
	 * @return str the String of the collection of link flows for each edge
	 */
	public String getStringLinkFlowsData() {
		String str = "";
		str += "Link | Iterations\n\n";
		for (int i = 0; i < this.linkFlowsData.get(0).length; i++) {
			for (int j = 0; j < this.linkFlowsData.get(0)[0].length; j++) {
				if (this.network.isEdgeInitial(i, j)) {
					str += "Link: (" + i + " " + j + ") ";
					for (int k = 0; k < this.linkFlowsData.size(); k++) {
						str += this.linkFlowsData.get(k)[i][j] + " ";
					}
					str += "\n\n";
				}
			}
		}
		return str;
	}

}