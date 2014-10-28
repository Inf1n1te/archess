package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import views.GuiView;

public class DataController implements ActionListener {

	// data controller has an GuiView and DataListener
	private GuiView view;
	private DataListener listener;
	private boolean listenerBooted;
	
	/**
	 * Instantiates the view
	 */
	public DataController() {
		System.out.println("[DataController] DataController has started..");
		view = new GuiView();
		System.out.println("[DataController] Booted up the view");
		// add the actionListeners on view
		addActionListeners(this);
		listenerBooted = false;
	}
	
	/** 
	 * Triggers the addActionListener function on view
	 * @param ae - the current dataController
	 */
	private void addActionListeners(DataController ae) {
		view.addActionListeners(ae);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		System.out.println("[DataController] Action has been performed..");
		JButton clickedButton = (JButton) ae.getSource();
		if (clickedButton.getName().equals("startListener")) {
			System.out.println("[DataController] startListener has been pushed, booting up DataListener..");
			if (!listenerBooted) {
				listener = new DataListener();
				listener.register(this);
				listener.start();
				listenerBooted = true;
			}
		}
	}
	
	public void onDataReceived(int[][] boardData) {
		System.out.println("[DataController] Data has been received by the listener");
		
		// print out the matrix for now
		for (int x = 0; x < 8; x++) {
			System.out.print("[");
			for (int y = 0; y < 8; y++) {
				System.out.print("[" + boardData[x][y] + "],");
			}
			System.out.print("]\n");
		} 
	}

}
