package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import us.unlv.sdue.model.Network;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class CreateNetworkAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private JFrame frmSdue;
	private JTextField textFieldNbNodes;
	
	private mxGraph graph;
	private ArrayList<Object> vertices;
	
	private Network network;
	private int numberNodes;

	public CreateNetworkAction(JFrame frmSdue, JTextField textFieldNbNodes, mxGraph graph, ArrayList<Object> vertices) {
		this.frmSdue = frmSdue;
		this.textFieldNbNodes = textFieldNbNodes;
		this.graph = graph;
		this.vertices = vertices;
	}
	
	public Network getNetwork(){
		return this.network;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.textFieldNbNodes.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this.frmSdue, "Number of Nodes should be initialized");
		} else {
			try {
				this.numberNodes = Integer.parseInt(this.textFieldNbNodes.getText());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.frmSdue, "Number of Nodes should be an Integer less than 30");
			}
			if (this.numberNodes > 30) {
				JOptionPane.showMessageDialog(this.frmSdue, "Number of Nodes should be less than 30");
				this.numberNodes = 0;
			} else {
				this.graph.getModel().beginUpdate();
				try {
					this.network = new Network(this.numberNodes);	
					
					this.graph.removeCells(this.vertices.toArray());
					this.vertices.removeAll(this.vertices);
					// Polygone Vertex Calculator
					double xCenter = 320;
					double yCenter = 125;
					int xRadius = 240;
					int yRadius = 120;
					double offset = 0;
					double theta = 2 * Math.PI / this.numberNodes + offset;
					for (int i = 0; i < this.numberNodes; ++i) {
						double x = xCenter + Math.cos(theta * i) * xRadius;
						double y = yCenter + Math.sin(theta * i) * yRadius;
						Object parent = this.graph.getDefaultParent();
						Object vertex = this.graph.insertVertex(parent, null, i, x, y, 25, 25,
								mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE);
						this.vertices.add(vertex);
					}
				} finally {
					this.graph.getModel().endUpdate();
				}
			}
		}
	}
	
}
