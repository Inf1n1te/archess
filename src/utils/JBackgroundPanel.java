package utils;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JBackgroundPanel extends JPanel {
	  private BufferedImage img;
	 
	  public JBackgroundPanel(LayoutManager manager, String image) {
		  super(manager);
	    // load the background image
	    try {
	      img = ImageIO.read(getClass().getResource("/images/" + image));
	    } catch(IOException e) {
	      e.printStackTrace();
	    }
	  }
	 
	  @Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    // paint the background image and scale it to fill the entire space
	    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	  }
}