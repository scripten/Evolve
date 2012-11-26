package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

class MainCanvas extends JPanel
{
	private BufferedImage i;


	public MainCanvas(BufferedImage i) {
		this.i = i;
		setBounds(0, 0, i.getWidth(), i.getHeight());
		setSize(i.getWidth(), i.getHeight());
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
	}

	@Override
	public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
        g.drawImage(i, 0, 0, Color.red, null);
    }
}

public class WritingPixelsDemonstration {
	public static int WIDTH = 525;
	public static int HEIGHT = 370;

	public static int APP_WIDTH = WIDTH;
	public static int APP_HEIGHT = HEIGHT + 22; // decoration height


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedImage i = new BufferedImage(525, 370, BufferedImage.TYPE_INT_ARGB);

        JFrame f = new JFrame( "WritableRaster (setting pixels)" );
        f.add(new MainCanvas(i));
        f.setSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color color = new Color(50, 100, 200);

        long time = -1000;
        while (true) {
        	long current = System.currentTimeMillis();
        	if ((current - time) > 250) {
        		fillI(i, color);
        		f.repaint();
        		color = new Color(color.getGreen(), color.getBlue(), color.getRed());
        		time = current;
        	}
        }

	}


	private static void fillI(BufferedImage i, Color base) {
		for (int row = 0; row < i.getWidth(); ++row) {
			int x = row + i.getMinX();
			for (int col = 0; col < i.getHeight(); ++col) {
				int y = col + i.getMinY();

				i.setRGB(x, y, base.getRGB());
			}
		}
	}

}
