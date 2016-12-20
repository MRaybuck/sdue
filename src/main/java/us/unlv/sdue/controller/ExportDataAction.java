package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class ExportDataAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private JFrame frmSdue;
	private RunAction runAction;
	private File file; // stocks the current file
	
	public ExportDataAction(JFrame frmSdue, RunAction runAction) {
		this.frmSdue = frmSdue;
		this.runAction = runAction;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!this.runAction.isPerformed()) {
			JOptionPane.showMessageDialog(this.frmSdue, "Simulation should be run");
		} else {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setCurrentDirectory(this.file);
			jFileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT (*.txt)", new String[] {"txt"});
			jFileChooser.setFileFilter(filter);
			int result = jFileChooser.showSaveDialog(this.frmSdue);
			if (result == JFileChooser.APPROVE_OPTION) {
				FileWriter fileWriter = null;
				try {
					String fileName = jFileChooser.getSelectedFile().getAbsolutePath() + "." + filter.getExtensions()[0];
					this.file = new File(fileName);
					fileWriter = new FileWriter(this.file);
					switch (e.getActionCommand()) {
						case "Times":
							String stringTimesData = this.runAction.getStringTimesData();
							fileWriter.write(stringTimesData);
							break;
						case "Flows":
							String stringFlowsData = this.runAction.getStringFlowsData();
							fileWriter.write(stringFlowsData);
							break;
						case "Link Flows":
							String stringLinkFlowsData = this.runAction.getStringLinkFlowsData();
							fileWriter.write(stringLinkFlowsData);
							break;
						default:
					}
					fileWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
