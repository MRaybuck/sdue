package us.unlv.sdue.model;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Inspired by Dynamic Stochastic Network Loading algorithm of Stochastic Dynamic User Equilibrium
 * Using a Mixed Logit Modeling Framework, Alexander PAZ.
 * This algorithm implements a mixed logit route-choice model at the network level.
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class MixedLogitLoading {
	
	/**
	 * 
	 * @param network
	 * @param networkInitial
	 * @param Origine
	 * @param Destination
	 * @param q
	 * @return
	 */
	public double[][] load(Network network, Network networkInitial, int Origine, int Destination,
			int q) {
		
		/**********************************/
		/***** Step 0: Initialization *****/
		/**********************************/
		
		int numberNodes = network.size();

		int h = 1;
		double[][] T = new double[numberNodes][numberNodes];
		double[][] L = new double[numberNodes][numberNodes];
		double[][][] x = new double[10][numberNodes][numberNodes];
		double[] C = new double[numberNodes];
		double[] Co = new double[numberNodes];
		double[][] W = new double[numberNodes][numberNodes];
		int[] ascending = new int[numberNodes];
		double[] tmp = new double[numberNodes];
		int H = 2;// number of iteration
		int i, j;
		// step 1 : Calculation of link likelihood
		while (h < H) {
			
			/************************************************/
			/**** Step 1: Calculation of link likelihoods ***/
			/************************************************/

			for (i = 1; i < numberNodes; i++) {
				for (j = 1; j < numberNodes; j++) {
					if (network.getWeight(i, j) != 0) {
						T[i][j] = network.getWeight(i, j) + computeEps(network, i, j);// compute
																							// a
																							// travel
																							// time
																							// based
																							// on
																							// a
																							// normal
																							// distribution
						network.setWeight(i, j, T[i][j]);

					}
				}
			}

//			Co = networkInitial.dijkstraVali(networkInitial, Destination);
//			C = networkInitial.dijkstraVali(network, Destination);

			// Compute the link likelihood
			for (i = 1; i < numberNodes; i++) {
				for (j = 1; j < numberNodes; j++) {
					if (T[i][j] != 0 && Co[j] < Co[i]) {// if Co(j)<Co(i) :
														// L(i>j)=exp(theta*(C(i)-C(j)-T(i>j)))

						L[i][j] = Math.exp(C[i] - C[j] - T[i][j]);
					} else {
						L[i][j] = 0;// otherwise : L(i>j)=0
					}

				}
			}
			
			/********************************/
			/**** Step 2: Backward pass ****/
			/********************************/

			// Consider nodes in ascending order of s(i)
			// Ascending order saved in ascending[]
			int node, m;
			double sum;

			ascending[0] = Destination;
			for (i = 1; i < numberNodes; i++) {
				tmp[i] = C[i];
			}
			for (i = 1; i < numberNodes; i++) {
				ascending[i] = getMinimalValue(tmp, numberNodes);
				// System.out.println(ascending[i]);
			}
			// for each node i, compute the link flow
			for (j = 1; j < numberNodes; j++) {

				for (i = 1; i < numberNodes; i++) {
					if (L[i][j] == 0) {
						W[i][j] = 0;
					}
				}
				node = ascending[j];// consider link in ascending order

				ArrayList<Integer> F = network.getArrivingNodes(node);// get nodes
															// arriving on
															// current node
				for (i = 0; i < F.size(); i++) {// consider nodes arriving on
												// current node only
					if (node == Destination) {// if j=Destination :
												// x(i>j)=q*w(i>j)/sum(w(m>j)) m
												// : node arriving on j

						W[F.get(i)][node] = L[F.get(i)][node];
						// System.out.println(W[F[i]][node]);
					} else {// otherwise : x(i>j) = sum(x(j>m)) *
							// w(i>j)/sum(w(n>j)) m : node leaving j, n : node
							// arriving on j
						ArrayList<Integer> O = network.getLeavingNodes(node);

						sum = 0;
						for (m = 0; m < O.size(); m++) {
							sum = sum + W[node][O.get(m)];
						}

						W[F.get(i)][node] = L[F.get(i)][node] * sum;

					}
				}
			}
			
			/*******************************/
			/**** Step 3: Forward pass *****/
			/*******************************/

			// Consider nodes in descending order of C
			// Ascending order saved in ascending[]
			ascending[0] = Origine;
			for (i = 0; i < numberNodes; i++) {
				tmp[i] = C[i];
			}
			for (i = 1; i < numberNodes; i++) {
				ascending[i] = getMaximalValue(tmp, numberNodes);

			}

			for (j = 1; j < numberNodes; j++) {
				node = ascending[j];

				ArrayList<Integer> F = network.getLeavingNodes(node);

				for (i = 0; i < F.size(); i++) {
					if (node == Origine) {
						sum = 0;
						for (m = 0; m < F.size(); m++) {
							sum += W[node][F.get(m)];

						}

						if (sum == 0) {
							x[h][node][F.get(i)] = 0;
						} else {
							x[h][node][F.get(i)] = q * W[node][F.get(i)] / sum;
						}

					} else {
						ArrayList<Integer> O = network.getArrivingNodes(node);
						x[h][node][F.get(i)] = 0;
						sum = 0;
						for (m = 0; m < O.size(); m++) {
							x[h][node][F.get(i)] += x[h][O.get(m)][node];

						}

						for (m = 0; m < F.size(); m++) {
							sum += W[node][F.get(m)];
						}

						if (sum == 0) {

							x[h][node][F.get(i)] = 0;

						} else {

							x[h][node][F.get(i)] = x[h][node][F.get(i)] * W[node][F.get(i)] / sum;
						}

					}
				}
			}

			for (i = 1; i < numberNodes; i++) {
				for (j = 1; j < numberNodes; j++) {
					x[h][i][j] = ((h - 1) * x[h - 1][i][j] + x[h][i][j]) / h;
				}
			}
			h++;

		}

		return x[h - 1];
	}

	public static int getMinimalValue(double[] tab, int size) {
		double min = tab[1];
		int index = 1;
		for (int i = 1; i < size; i++) {
			if (min > tab[i]) {
				min = tab[i];
				index = i;
			}
		}
		tab[index] = Integer.MAX_VALUE;
		return index;
	}

	public static int getMaximalValue(double[] tab, int size) {
		double max = tab[1];
		int index = 1;
		for (int i = 1; i < size; i++) {
			if (max < tab[i]) {
				max = tab[i];
				index = i;
			}
		}
		tab[index] = Integer.MIN_VALUE;
		return index;
	}

	private double computeEps(Network network, int i, int j) {
		double beta = 1.0;
		double res_alea;
		double eps = Math.sqrt(beta * network.getWeightInitial(i, j));
		NormalDistribution normaldist = new NormalDistribution(0, 1);
		res_alea = normaldist.sample();
		while (res_alea <= 0) {
			res_alea = normaldist.sample();
		}
		double epsilon = eps * res_alea;
		return epsilon;
	}
	
}
