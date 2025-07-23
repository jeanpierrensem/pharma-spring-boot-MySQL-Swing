package com.officine.losto.uti.shared;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/*Mettre une image de fond */
public class ImageDesktopPane extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	public static final int TILED = 0;
	public static final int SCALED = 1;
	public static final int ACTUAL = 2;

	private BufferedImage image;
	private int style;
	private float alignmentX = 0.5f;
	private float alignmentY = 0.5f;

	public ImageDesktopPane(String image) {
		this(image, TILED);
	}

	public ImageDesktopPane(String image, int style) {
		try {
			/*
			 * this.image = javax.imageio.ImageIO.read(new
			 * java.io.File(frameConnexion.class.getResource( image).getFile()));
			 */
			if (image == null) {
				this.image = javax.imageio.ImageIO.read(new java.io.File("fond.jpg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				this.image = javax.imageio.ImageIO.read(new java.io.File("fond.jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.repaint();
			e.printStackTrace();
		}
		;
		this.style = style;
		// setLayout(new BorderLayout());
	}

	public void setImageAlignmentX(float alignmentX) {
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
	}

	public void setImageAlignmentY(float alignmentY) {
		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;

	}

	public void add(JComponent component) {
		add(component, null);
	}

	public void add(JComponent component, Object constraints) {
		component.setOpaque(false);

		if (component instanceof JScrollPane) {
			JScrollPane scrollPane = (JScrollPane) component;
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque(false);
			Component c = viewport.getView();

			if (c instanceof JComponent) {
				((JComponent) c).setOpaque(false);
				System.out.println("c'est un jComponent");
			}
		}

		super.add(component, constraints);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image == null) {
			g.setColor(Color.RED);
			g.drawString("", 50, 50);
			return;
		}

		switch (style) {
		case TILED:
			drawTiled(g);
			break;

		case SCALED:
			Dimension d = getSize();
			g.drawImage(image, 0, 0, d.width, d.height, null);
			break;

		case ACTUAL:
			drawActual(g);
			break;
		}
	}

	private void drawTiled(Graphics g) {
		Dimension d = getSize();
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		for (int x = 0; x < d.width; x += width) {
			for (int y = 0; y < d.height; y += height) {
				g.drawImage(image, x, y, null, null);
			}
		}
	}

	private void drawActual(Graphics g) {
		Dimension d = getSize();
		float x = (d.width - image.getWidth()) * alignmentX;
		float y = (d.height - image.getHeight()) * alignmentY;
		g.drawImage(image, (int) x, (int) y, this);
	}
}
