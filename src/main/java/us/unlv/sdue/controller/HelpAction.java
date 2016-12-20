package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class HelpAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		String message = "To create a network:\n";
		message += "- Insert Number of Nodes the number of nodes of the network between 0 and 30\n";
		message += "- Click on Create Network\n";
		message += "- Insert Source the source node number of the edge\n";
		message += "- Insert Target the target node number of the edge\n";
		message += "- Click on Add Edge\n\n";
		message += "To run the simulation:\n";
		message += "- Choose your model between Logit, Probit and Mixed-Logit\n";
		message += "- Origin the origin node number\n";
		message += "- Insert Destination the destination node number\n";
		message += "- Insert Trip Rate the flow of trips per time unit\n";
		message += "- Insert Parameter Theta the parameter used to calculate link likelihoods\n";
		message += "- Click on Run\n";
		JOptionPane.showMessageDialog(null, message, "Help SDUE", JOptionPane.INFORMATION_MESSAGE);
	}

}
