package us.unlv.sdue;

import java.awt.EventQueue;

import us.unlv.sdue.view.Window;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class Main {

	/**
	 * Launches the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
