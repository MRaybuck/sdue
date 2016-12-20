package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class AboutUsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		String message = "Thank you for trying our app!\n";
		message += "Authors: Olivier BOURDON & Maxime LENORMAND\n";
		JOptionPane.showMessageDialog(null, message, "About us", JOptionPane.INFORMATION_MESSAGE);
	}

}
