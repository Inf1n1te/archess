package controllers;

import java.util.HashMap;

import views.GuiView;

public class ReplayController extends Thread {
	
	private HashMap moves;
	private GuiView view;
	
	public void setMoves(HashMap moves) {
		this.moves = moves;
	}
	
	public void setView(GuiView view) {
		this.view = view;
	}
	
	public void run() {
		System.out.println("replaying");
		for (int i = 0; i < moves.size(); i ++) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int[] move = (int[]) moves.get(i);
			int[] oldcords = new int[]{move[0], move[1]};
			int[] newcords = new int[]{move[2], move[3]};
			view.redrawBoard(oldcords, newcords, true);
		} 
		System.out.println("Done replaying");
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
