package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class WorldCanvas extends JPanel {
	private BufferedImage worldImage;
	/**
	 * Create the panel.
	 */
	public WorldCanvas() {
		setBounds(0, 0, 550, 350);
	}

	public void setWorldImage(BufferedImage worldImage) {
		this.worldImage = worldImage;
		setBounds(0, 0, worldImage.getWidth(), worldImage.getHeight());
		setSize(worldImage.getWidth(), worldImage.getHeight());
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());
	}

	@Override
	public void paintComponent(Graphics g)
    {
		super.paintComponent(g);
		if (worldImage != null)
			g.drawImage(worldImage, 0, 0, Color.black, null);
    }

}
