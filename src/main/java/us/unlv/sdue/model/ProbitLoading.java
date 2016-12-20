package us.unlv.sdue.model;

import org.apache.commons.math3.distribution.NormalDistribution;


import us.unlv.sdue.model.Network;

/**
 * Inspired by the Monte Carlo Simulation Algorithm p.301 of Urban Transportation Networks, Yosef SHEFFI.
 * Monte Carlo Simulation Algorithm implements a probit route-choice model at the network level.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class ProbitLoading {

	/**
	 * Loads link flows to a network according to a probit route-choice model.
	 * @param network the network of interest
	 * @param origin the origin node number
	 * @param destination the destination node number
	 * @param tripRate the flow of trips per time unit
	 * @param beta the variance of the perceived travel time over a road segment of unit travel time at free flow
	 * @return x the link flows after the loading
	 */
	public double[][] load(Network network, int origin, int destination, int tripRate, double beta) {
		
		/**********************************/
		/***** Step 0: Initialization *****/
		/**********************************/
		
		int numberNodes = network.size();
		// the perceived link travel-time vectors
		Network T = new Network(numberNodes);
		// link flows
		double[][][] x = new double[200000][numberNodes][numberNodes];
		// link flows resulting from the applications of the sampling-assignment sequence
		double[][][] X = new double[200000][numberNodes][numberNodes];
		// standard deviations of x
		double[][] sig = new double[numberNodes][numberNodes];
		for (int i = 0; i < numberNodes; i++) {
			for (int j = 0; j < numberNodes; j++) {
				sig[i][j] = Double.MAX_VALUE;
			}
		}
		
		int l = 1;
		double sample;
		boolean stopTest = false;

		while (!stopTest){
			for (int i = 0; i < numberNodes; i++) {
				for (int j = 0; j < numberNodes; j++) {
					if (network.getWeight(i, j) != 0) {

						/**********************************/
						/******** Step 1: Sampling ********/
						/**********************************/
						
						NormalDistribution normaldistribution = new NormalDistribution(network.getWeight(i, j),
								beta * network.getWeight(i, j));

						// makes sure the new time is positive
						sample = normaldistribution.sample();
						while (sample <= 0) {	
							sample = normaldistribution.sample();
						}

						T.setWeight(i, j, sample);
					}
				}
			}

			/*********************************************/
			/***** Step 2: All-or-nothing assignment *****/
			/*********************************************/
			
			PathsCollection pathsCollection = new PathsCollection(T, origin, destination);
			Path shortestPath = new Path(T);
			shortestPath = pathsCollection.findShortestPath();
			
			for (int i = 0; i < shortestPath.size() - 1; i++) {
				int a = shortestPath.getNodes().get(i);
				int b = shortestPath.getNodes().get(i + 1);
				X[l][a][b] = tripRate;
			}
			
			/**********************************/
			/***** Step 3: Flow averaging *****/
			/**********************************/
			
			for (int i = 0; i < numberNodes; i++) {
				for (int j = 0; j < numberNodes; j++) {
					x[l][i][j] = (((l - 1) * x[l - 1][i][j]) + X[l][i][j]) / l;
				}
			}

			/**********************************/
			/***** Step 4: Stopping test *****/
			/**********************************/

			double sum;
			double max = 0;
			double test = 0;
			for (int i = 0; i < numberNodes; i++) {
				for (int j = 0; j < numberNodes; j++) {
					sum = 0;
					for (int m = 0; m < l; m++) {
						sum += (X[m][i][j] - x[l][i][j]) * (X[m][i][j] - x[l][i][j]);
					}
					sig[i][j] = Math.sqrt(sum / (l * (l - 1)));
					test = sig[i][j] / x[l][i][j];
					if (test > max) {
						max = test;
					}

				}
			} 
			if (max <= 0.2) {
				stopTest = true;
			} else {
				l += 1;
			}

		} 

		return x[l];
		
	}

}
