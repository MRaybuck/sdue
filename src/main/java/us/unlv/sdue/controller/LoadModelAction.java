package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class LoadModelAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private JLabel lblParameter;
	
	private String currentModel;

	public LoadModelAction(JLabel lblParameter) {
		this.lblParameter = lblParameter;
		this.currentModel = "Logit";
	}
	
	public String getCurrentModel(){
		return this.currentModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "Logit":
				this.lblParameter.setText("Parameter Theta:");
				this.currentModel = "Logit";
				break;
			case "Probit":
				this.lblParameter.setText("Parameter Beta:");
				this.currentModel = "Probit";
				break;
			case "Mixed-Logit":
				this.lblParameter.setText("Parameter Theta:");
				this.currentModel = "Mixed-Logit";
				break;
			default:
		}
	}

}
