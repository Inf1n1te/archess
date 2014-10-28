package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import views.GuiView;

public class DataController implements ActionListener {

	// data controller has an GuiView and DataListener
	private GuiView view;
	private DataListener listener;
	
	/**
	 * Instantiates the view
	 */
	public DataController() {
		System.out.println("[DataController] DataController has started..");
		view = new GuiView();
		System.out.println("[DataController] Booted up the view");
		// add the actionListeners on view
		addActionListeners(this);
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
			listener = new DataListener();
		}
	}

}
