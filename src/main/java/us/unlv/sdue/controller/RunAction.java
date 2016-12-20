package us.unlv.sdue.controller;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import us.unlv.sdue.model.Network;
import us.unlv.sdue.model.Path;
import us.unlv.sdue.model.PathsCollection;
import us.unlv.sdue.model.Solution;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class RunAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private JFrame frmSdue;
	private JTextField textFieldOrigin;
	private JTextField textFieldDestination;
	private JTextField textFieldTripRate;
	private JTextField textFieldNumberIterations;
	private JTextField textFieldParameter;
	
	private ArrayList<Object> vertices;
	private XYSeriesCollection dataset;
	
	private Network network;
	private int origin;
	private int destination;
	private int tripRate;
	private int numberIterations;
	private double parameter;
	
	private boolean isPerformed;
	private String stringTimesData;
	private String stringFlowsData;
	private String stringLinkFlowsData;
	
	private CreateNetworkAction createNetworkAction;
	private LoadModelAction loadModelAction;

	public RunAction(JFrame frmSdue, JTextField textFieldOrigin, JTextField textFieldDestination,
			JTextField textFieldTripRate, JTextField textFieldNumberIterations, JTextField textFieldParameter,
			ArrayList<Object> vertices, XYSeriesCollection dataset, CreateNetworkAction createNetworkAction,
			LoadModelAction loadModelAction) {
		this.frmSdue = frmSdue;
		this.textFieldOrigin = textFieldOrigin;
		this.textFieldDestination = textFieldDestination;
		this.textFieldTripRate = textFieldTripRate;
		this.textFieldNumberIterations = textFieldNumberIterations;
		this.textFieldParameter = textFieldParameter;
		this.vertices = vertices;
		this.dataset = dataset;
		this.createNetworkAction = createNetworkAction;
		this.loadModelAction = loadModelAction;
	}
	
	public String getStringTimesData(){
		return this.stringTimesData;
	}
	
	public String getStringFlowsData(){
		return this.stringFlowsData;
	}
	
	public String getStringLinkFlowsData(){
		return this.stringLinkFlowsData;
	}
	
	public boolean isPerformed(){
		return this.isPerformed;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.vertices.isEmpty()) {
			JOptionPane.showMessageDialog(this.frmSdue, "Network should be created");
		} else if ((this.textFieldOrigin.getText().isEmpty()) || (this.textFieldDestination.getText().isEmpty())
				|| (this.textFieldTripRate.getText().isEmpty()) || (this.textFieldNumberIterations.getText().isEmpty())
				|| (this.textFieldParameter.getText().isEmpty())) {
			JOptionPane.showMessageDialog(this.frmSdue,
					"Origin, Destination, Trip Rate, Number of Iterations and Parameter Theta should be initialized");
		} else {
			try {
				this.origin = Integer.parseInt(this.textFieldOrigin.getText());
				this.destination = Integer.parseInt(this.textFieldDestination.getText());
				this.tripRate = Integer.parseInt(this.textFieldTripRate.getText());
				this.numberIterations = Integer.parseInt(this.textFieldNumberIterations.getText());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.frmSdue,
						"Origin, Destination, Trip Rate and Number of Iterations should be an Integer");
			}
			try {
				this.parameter = Double.parseDouble(this.textFieldParameter.getText());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.frmSdue, "Parameter Theta should be a Double");
			}
			this.network = this.createNetworkAction.getNetwork();
			if ((this.origin < 0) || (this.origin > this.network.size() - 1) || (this.destination < 0)
					|| (this.destination > this.network.size() - 1)) {
				JOptionPane.showMessageDialog(this.frmSdue, "Origin or Destination doesn't exist");
			} else {
				PathsCollection pathsCollection = new PathsCollection(this.network, this.origin, this.destination);
				System.out.println("All paths:\n" + pathsCollection);
				Path shortestPath = pathsCollection.findShortestPath();
				System.out.println("Shortest path:\n" + shortestPath + "\n");
				
				Solution solution = new Solution();
				String currentModel = this.loadModelAction.getCurrentModel();
				this.frmSdue.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				ArrayList<double[]> timesData = solution.run(this.network, currentModel, this.origin, this.destination,
						this.tripRate, this.numberIterations, this.parameter);
				this.isPerformed = true;
				this.stringTimesData = solution.getStringTimesData();
				this.stringFlowsData = solution.getStringFlowsData();
				this.stringLinkFlowsData = solution.getStringLinkFlowsData();
				this.updateChart(timesData);
				this.frmSdue.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * Updates the chart.
	 * @param timesData the collection of travel times for each path
	 */
	private void updateChart(ArrayList<double[]> timesData) {
		this.dataset.removeAllSeries();
		for (int j = 0; j < timesData.get(0).length; j++) {
			XYSeries myline = new XYSeries("Path n°" + (j + 1));
			for (int i = 0; i < timesData.size(); i++)
				myline.add(i + 1, timesData.get(i)[j]);
			this.dataset.addSeries(myline);
		}
	}
	
}
