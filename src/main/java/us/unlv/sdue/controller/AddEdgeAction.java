package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mxgraph.view.mxGraph;

import us.unlv.sdue.model.Network;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class AddEdgeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private JFrame frmSdue;
	private JTextField textFieldSource;
	private JTextField textFieldTarget;
	private JTextField textFieldWeight;
	
	private mxGraph graph;
	private ArrayList<Object> vertices;
	
	private Network network;
	private int source;
	private int target;
	private double weight;

	private CreateNetworkAction createNetworkAction;

	public AddEdgeAction(JFrame frmSdue, JTextField textFieldSource, JTextField textFieldTarget,
			JTextField textFieldWeight, mxGraph graph, ArrayList<Object> vertices,
			CreateNetworkAction createNetworkAction) {
		this.frmSdue = frmSdue;
		this.textFieldSource = textFieldSource;
		this.textFieldTarget = textFieldTarget;
		this.textFieldWeight = textFieldWeight;
		this.graph = graph;
		this.vertices = vertices;
		this.createNetworkAction = createNetworkAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.vertices.isEmpty()) {
			JOptionPane.showMessageDialog(this.frmSdue, "Network should be created");
		} else if ((this.textFieldSource.getText().isEmpty()) || (this.textFieldTarget.getText().isEmpty())
				|| (this.textFieldWeight.getText().isEmpty())) {
			JOptionPane.showMessageDialog(this.frmSdue, "Source, Target and Weight should be initialized");
		} else {
			try {
				this.source = Integer.parseInt(this.textFieldSource.getText());
				this.target = Integer.parseInt(this.textFieldTarget.getText());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.frmSdue, "Source and Destination should be an Integer");
			}
			try {
				this.weight = Double.parseDouble(this.textFieldWeight.getText());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.frmSdue, "Weight should be a Double");
			}
			this.network = this.createNetworkAction.getNetwork();
			if ((this.source < 0) || (this.source > this.network.size() - 1) || (this.target < 0)
					|| (this.target > this.network.size() - 1)) {
				JOptionPane.showMessageDialog(this.frmSdue, "Source or Target doesn't exist");
			} else {
				this.graph.getModel().beginUpdate();
				try {
					this.network.addEdge(this.source, this.target, this.weight);
					this.network.addEdgeInitial(this.source, this.target, this.weight);
					System.out.println("Network:\n" + this.network);
					
					Object parent = this.graph.getDefaultParent();
					this.graph.insertEdge(parent, null, this.weight, this.vertices.get(this.source),
							this.vertices.get(this.target));
				} finally {
					this.graph.getModel().endUpdate();
				}
			}
		}
	}
	
}
