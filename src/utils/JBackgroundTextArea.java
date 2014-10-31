package utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class JBackgroundTextArea extends JTextArea {
	  private BufferedImage img;
	private TexturePaint texturePaint;
		 
	  public JBackgroundTextArea(String image) {
	    // load the background image
	    try {
	      img = ImageIO.read(getClass().getResource("/images/" + image));
	    } catch(IOException e) {
	      e.printStackTrace();
	    }
	    Rectangle rect = new Rectangle(0, 0, img.getWidth(null), img.getHeight(null));
	    texturePaint = new TexturePaint(img, rect);
	    setOpaque(false);
	  }
	 
	  /**
	   * Override the painComponent method to do our image drawing.
	   */
	  public void paintComponent(Graphics g)
	  {
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setPaint(texturePaint);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    super.paintComponent(g);
	  }
	 
}
